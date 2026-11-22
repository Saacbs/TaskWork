package dao;

import model.Usuario;

import util.Banco;

import java.sql.*;
import java.util.*;

public class UsuarioDAO {
    private static final String CONSULTA =
            "SELECT U.*, E.NOME EMPRESA_NOME FROM USUARIO U LEFT JOIN EMPRESA E ON"
                    + " E.ID=U.EMPRESA_ID";

    public void salvar(Usuario usuario) {
        salvar(usuario, null);
    }

    public void salvar(Usuario usuario, Usuario ator) {
        try (Connection c = Banco.conectar()) {
            salvar(c, usuario, ator);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Não foi possível salvar. Confira o e-mail e os dados informados.", e);
        }
    }

    public void salvar(Connection c, Usuario u) throws SQLException {
        salvar(c, u, null);
    }

    private void salvar(Connection c, Usuario u, Usuario ator) throws SQLException {
        boolean restrito = ator != null && !util.Seguranca.master(ator);
        if (restrito
                && (u.getEmpresaId() != ator.getEmpresaId()
                        || !Arrays.asList("Colaborador", "Atendente").contains(u.getPerfil())))
            throw new SQLException("Perfil ou empresa não permitido.");
        try (PreparedStatement verificar =
                c.prepareStatement(
                        "SELECT ID FROM USUARIO WHERE LOWER(EMAIL)=LOWER(?) AND ID<>?")) {
            verificar.setString(1, u.getEmail());
            verificar.setInt(2, u.getId());
            try (ResultSet rs = verificar.executeQuery()) {
                if (rs.next()) throw new SQLException("E-mail já cadastrado.");
            }
        }
        boolean novo = u.getId() == 0;
        String sql =
                novo
                        ? "INSERT INTO USUARIO (NOME,EMAIL,TELEFONE,EMPRESA_ID,PERFIL,ATIVO,SENHA)"
                                + " VALUES (?,?,?,?,?,?,?)"
                        : "UPDATE USUARIO SET VERSAO_SESSAO=VERSAO_SESSAO+CASE WHEN ATIVO<>? OR"
                                + " PERFIL<>? OR COALESCE(SENHA, '')<>COALESCE(?, '') THEN 1 ELSE 0"
                                + " END,"
                                + " NOME=?,EMAIL=?,TELEFONE=?,EMPRESA_ID=?,PERFIL=?,ATIVO=?,SENHA=?"
                                + " WHERE ID=?"
                                + (restrito
                                        ? " AND EMPRESA_ID=? AND PERFIL IN"
                                                + " ('Colaborador','Atendente')"
                                        : "");
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int indice = 1;
            if (!novo) {
                ps.setBoolean(indice++, u.isAtivo());
                ps.setString(indice++, u.getPerfil());
                ps.setString(indice++, u.getSenha());
            }
            ps.setString(indice++, u.getNome());
            ps.setString(indice++, u.getEmail());
            ps.setString(indice++, u.getTelefone());
            if (u.getEmpresaId() == 0) ps.setNull(indice++, Types.INTEGER);
            else ps.setInt(indice++, u.getEmpresaId());
            ps.setString(indice++, u.getPerfil());
            ps.setBoolean(indice++, u.isAtivo());
            ps.setString(indice++, u.getSenha());
            if (!novo) {
                ps.setInt(indice++, u.getId());
                if (restrito) ps.setInt(indice++, ator.getEmpresaId());
            }
            if (ps.executeUpdate() != 1) throw new SQLException("Usuário não encontrado.");
            if (novo)
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    u.setId(rs.getInt(1));
                }
        }
    }

    public List<Usuario> listar() {
        return consultar("", null);
    }

    public List<Usuario> listarEmpresa(int id) {
        return consultar(" WHERE U.EMPRESA_ID=?", id);
    }

    public Usuario buscarPorId(int id) {
        List<Usuario> l = consultar(" WHERE U.ID=?", id);
        return l.isEmpty() ? null : l.get(0);
    }

    public Usuario buscarEmail(String email) {
        List<Usuario> l = consultar(" WHERE LOWER(U.EMAIL)=?", email);
        return l.isEmpty() ? null : l.get(0);
    }

    public boolean temMaster() {
        return !consultar(" WHERE U.PERFIL='Administrador master'", null).isEmpty();
    }

    private List<Usuario> consultar(String filtro, Object valor) {
        List<Usuario> lista = new ArrayList<>();
        try (Connection c = Banco.conectar();
                PreparedStatement ps = c.prepareStatement(CONSULTA + filtro + " ORDER BY U.NOME")) {
            if (valor != null) ps.setObject(1, valor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("ID"));
                    u.setNome(rs.getString("NOME"));
                    u.setEmail(rs.getString("EMAIL"));
                    u.setTelefone(rs.getString("TELEFONE"));
                    u.setEmpresaId(rs.getInt("EMPRESA_ID"));
                    u.setEmpresaNome(rs.getString("EMPRESA_NOME"));
                    u.setPerfil(rs.getString("PERFIL"));
                    u.setAtivo(rs.getBoolean("ATIVO"));
                    u.setSenha(rs.getString("SENHA"));
                    u.setVersaoSessao(rs.getInt("VERSAO_SESSAO"));
                    lista.add(u);
                }
            }
            return lista;
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível consultar usuários.", e);
        }
    }

    public void excluir(int id, Usuario ator) {
        boolean restrito = !util.Seguranca.master(ator);
        String sql =
                "DELETE FROM USUARIO WHERE ID=?"
                        + (restrito
                                ? " AND EMPRESA_ID=? AND PERFIL IN ('Colaborador','Atendente')"
                                : "");
        try (Connection c = Banco.conectar();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (restrito) ps.setInt(2, ator.getEmpresaId());
            if (ps.executeUpdate() != 1)
                throw new SQLException("Usuário indisponível ou perfil alterado.");
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Não foi possível excluir. Confira as permissões; se houver vínculos, inative o"
                            + " cadastro.",
                    e);
        }
    }
}
