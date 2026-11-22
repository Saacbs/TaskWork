package dao;

import model.*;

import util.Banco;
import util.Seguranca;

import java.sql.*;
import java.util.*;

public class SolicitacaoCadastroDAO {
    public void solicitar(SolicitacaoCadastro s) {
        try (Connection c = Banco.conectar()) {
            c.setAutoCommit(false);
            try {
                if (new UsuarioDAO().buscarEmail(s.getEmail()) != null)
                    throw new IllegalArgumentException("E-mail já cadastrado.");
                try (PreparedStatement ps =
                        c.prepareStatement(
                                "INSERT INTO SOLICITACAO"
                                        + " (NOME,EMAIL,TELEFONE,EMPRESA_ID,PERFIL,SENHA) VALUES"
                                        + " (?,?,?,?,?,?)")) {
                    ps.setString(1, s.getNome());
                    ps.setString(2, s.getEmail());
                    ps.setString(3, s.getTelefone());
                    ps.setInt(4, s.getEmpresaId());
                    ps.setString(5, s.getPerfil());
                    ps.setString(6, s.getSenha());
                    ps.executeUpdate();
                }
                try (PreparedStatement ps =
                        c.prepareStatement(
                                "INSERT INTO NOTIFICACAO (USUARIO_ID,TEXTO) SELECT ID,? FROM"
                                    + " USUARIO WHERE ATIVO=TRUE AND (PERFIL='Administrador master'"
                                    + " OR (PERFIL='Administrador' AND EMPRESA_ID=?))")) {
                    ps.setString(1, "Nova solicitação de cadastro aguardando análise.");
                    ps.setInt(2, s.getEmpresaId());
                    ps.executeUpdate();
                }
                c.commit();
            } catch (Exception e) {
                c.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Não foi possível solicitar. Confira os dados; o e-mail pode já ter uma"
                            + " solicitação.",
                    e);
        }
    }

    public List<SolicitacaoCadastro> listar(Usuario ator) {
        List<SolicitacaoCadastro> lista = new ArrayList<>();
        String sql =
                "SELECT S.*,E.NOME EMPRESA_NOME FROM SOLICITACAO S JOIN EMPRESA E ON"
                        + " E.ID=S.EMPRESA_ID"
                        + (Seguranca.master(ator) ? "" : " WHERE S.EMPRESA_ID=?")
                        + " ORDER BY S.ID DESC";
        try (Connection c = Banco.conectar();
                PreparedStatement ps = c.prepareStatement(sql)) {
            if (!Seguranca.master(ator)) ps.setInt(1, ator.getEmpresaId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SolicitacaoCadastro s = new SolicitacaoCadastro();
                    s.setId(rs.getInt("ID"));
                    s.setNome(rs.getString("NOME"));
                    s.setEmail(rs.getString("EMAIL"));
                    s.setTelefone(rs.getString("TELEFONE"));
                    s.setPerfil(rs.getString("PERFIL"));
                    s.setEmpresaId(rs.getInt("EMPRESA_ID"));
                    s.setEmpresaNome(rs.getString("EMPRESA_NOME"));
                    s.setSituacao(rs.getString("SITUACAO"));
                    s.setCriadoEm(rs.getString("CRIADO_EM"));
                    lista.add(s);
                }
            }
            return lista;
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível consultar solicitações.", e);
        }
    }

    public void decidir(int id, boolean aprovar, Usuario ator) {
        try (Connection c = Banco.conectar()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps =
                    c.prepareStatement("SELECT * FROM SOLICITACAO WHERE ID=? FOR UPDATE")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()
                            || !Seguranca.master(ator)
                                    && rs.getInt("EMPRESA_ID") != ator.getEmpresaId())
                        throw new IllegalArgumentException(
                                "Solicitação não encontrada para sua empresa.");
                    if (!"Pendente".equals(rs.getString("SITUACAO")))
                        throw new IllegalArgumentException("Solicitação já analisada.");
                    if (aprovar) {
                        Empresa empresa = new EmpresaDAO().buscarPorId(rs.getInt("EMPRESA_ID"));
                        if (empresa == null || !empresa.isAtivo())
                            throw new IllegalArgumentException("Empresa inativa.");
                        Usuario u = new Usuario();
                        u.setNome(rs.getString("NOME"));
                        u.setEmail(rs.getString("EMAIL"));
                        u.setTelefone(rs.getString("TELEFONE"));
                        u.setEmpresaId(rs.getInt("EMPRESA_ID"));
                        u.setPerfil(rs.getString("PERFIL"));
                        u.setSenha(rs.getString("SENHA"));
                        new UsuarioDAO().salvar(c, u);
                        try (PreparedStatement aviso =
                                c.prepareStatement(
                                        "INSERT INTO NOTIFICACAO (USUARIO_ID,TEXTO) VALUES"
                                                + " (?,?)")) {
                            aviso.setInt(1, u.getId());
                            aviso.setString(2, "Seu cadastro foi aprovado. Bem-vindo ao TaskWork.");
                            aviso.executeUpdate();
                        }
                    }
                }
                try (PreparedStatement atualizar =
                        c.prepareStatement("UPDATE SOLICITACAO SET SITUACAO=? WHERE ID=?")) {
                    atualizar.setString(1, aprovar ? "Aprovada" : "Rejeitada");
                    atualizar.setInt(2, id);
                    atualizar.executeUpdate();
                }
                c.commit();
            } catch (Exception e) {
                c.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Não foi possível analisar. Confira a situação, o e-mail e a empresa.", e);
        }
    }
}
