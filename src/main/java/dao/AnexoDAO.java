package dao;

import model.Anexo;
import model.Usuario;

import util.Banco;

import java.sql.*;
import java.util.*;

public class AnexoDAO {
    public void salvar(Connection c, int chamadoId, int autorId, List<Anexo> anexos)
            throws SQLException {
        for (Anexo a : anexos) {
            try (PreparedStatement ps =
                    c.prepareStatement(
                            "INSERT INTO ANEXO (CHAMADO_ID,NOME,TIPO,CONTEUDO,AUTOR_ID,CRIADO_EM)"
                                    + " VALUES (?,?,?,?,?,CURRENT_TIMESTAMP)")) {
                ps.setInt(1, chamadoId);
                ps.setString(2, a.getNome());
                ps.setString(3, a.getTipo());
                ps.setBytes(4, a.getConteudo());
                ps.setInt(5, autorId);
                ps.executeUpdate();
            }
        }
    }

    public List<Anexo> listar(int chamadoId) {
        List<Anexo> lista = new ArrayList<>();
        try (Connection c = Banco.conectar();
                PreparedStatement ps =
                        c.prepareStatement(
                                "SELECT"
                                    + " A.ID,A.CHAMADO_ID,A.NOME,A.TIPO,A.AUTOR_ID,A.CRIADO_EM,U.NOME"
                                    + " AUTOR_NOME FROM ANEXO A LEFT JOIN USUARIO U ON"
                                    + " U.ID=A.AUTOR_ID WHERE CHAMADO_ID=? ORDER BY A.ID")) {
            ps.setInt(1, chamadoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(montar(rs));
            }
            return lista;
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível listar anexos.", e);
        }
    }

    public Anexo buscar(int id) {
        try (Connection c = Banco.conectar();
                PreparedStatement ps =
                        c.prepareStatement(
                                "SELECT A.*,U.NOME AUTOR_NOME FROM ANEXO A LEFT JOIN USUARIO U ON"
                                        + " U.ID=A.AUTOR_ID WHERE A.ID=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Anexo a = montar(rs);
                a.setConteudo(rs.getBytes("CONTEUDO"));
                return a;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível buscar o anexo.", e);
        }
    }

    public void excluir(Anexo anexo, Usuario ator) {
        try (Connection c = Banco.conectar()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement ps =
                        c.prepareStatement(
                                "DELETE FROM ANEXO WHERE ID=? AND CHAMADO_ID=? AND (AUTOR_ID=? OR"
                                        + " ?=TRUE)")) {
                    ps.setInt(1, anexo.getId());
                    ps.setInt(2, anexo.getChamadoId());
                    ps.setInt(3, ator.getId());
                    ps.setBoolean(4, util.Seguranca.administrador(ator));
                    if (ps.executeUpdate() != 1)
                        throw new IllegalArgumentException(
                                "Anexo indisponível ou exclusão não permitida.");
                }
                new HistoricoDAO()
                        .adicionar(
                                c,
                                anexo.getChamadoId(),
                                ator.getId(),
                                "Anexo excluído: " + anexo.getNome(),
                                false);
                try (PreparedStatement ps =
                        c.prepareStatement(
                                "UPDATE CHAMADO SET ATUALIZADO_EM=CURRENT_TIMESTAMP WHERE ID=?")) {
                    ps.setInt(1, anexo.getChamadoId());
                    ps.executeUpdate();
                }
                c.commit();
            } catch (Exception e) {
                c.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Não foi possível excluir o anexo.", e);
        }
    }

    private Anexo montar(ResultSet rs) throws SQLException {
        Anexo a = new Anexo();
        a.setId(rs.getInt("ID"));
        a.setChamadoId(rs.getInt("CHAMADO_ID"));
        a.setNome(rs.getString("NOME"));
        a.setTipo(rs.getString("TIPO"));
        a.setAutorId(rs.getInt("AUTOR_ID"));
        a.setAutorNome(rs.getString("AUTOR_NOME"));
        a.setCriadoEm(rs.getTimestamp("CRIADO_EM"));
        return a;
    }
}
