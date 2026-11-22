package dao;

import model.*;

import util.Banco;

import java.sql.*;
import java.util.*;

public class NotificacaoDAO {
    public void avisar(Connection c, Chamado chamado, int autorId, String texto, boolean interno)
            throws SQLException {
        String sql =
                "INSERT INTO NOTIFICACAO (USUARIO_ID,CHAMADO_ID,TEXTO) SELECT ID,?,? FROM USUARIO"
                        + " WHERE ATIVO=TRUE AND ID<>? AND (ID=? OR (ID=? AND PERFIL IN"
                        + " ('Administrador','Atendente')) OR PERFIL='Administrador master' OR"
                        + " (EMPRESA_ID=? AND PERFIL IN ('Administrador','Atendente')))";
        try (PreparedStatement ps =
                c.prepareStatement(
                        sql
                                + (interno
                                        ? " AND PERFIL IN"
                                                + " ('Administrador','Atendente','Administrador"
                                                + " master') AND ID<>?"
                                        : ""))) {
            ps.setInt(1, chamado.getId());
            ps.setString(2, texto);
            ps.setInt(3, autorId);
            ps.setInt(4, chamado.getSolicitanteId());
            ps.setInt(5, chamado.getResponsavelId());
            ps.setInt(6, chamado.getEmpresaId());
            if (interno) ps.setInt(7, chamado.getSolicitanteId());
            ps.executeUpdate();
        }
    }

    public List<Notificacao> listar(Usuario usuario) {
        List<Notificacao> lista = new ArrayList<>();
        try (Connection c = Banco.conectar();
                PreparedStatement ps =
                        c.prepareStatement(
                                "SELECT N.* FROM NOTIFICACAO N LEFT JOIN CHAMADO C ON"
                                        + " C.ID=N.CHAMADO_ID WHERE N.USUARIO_ID=?"
                                        + (util.Seguranca.master(usuario)
                                                ? ""
                                                : " AND (N.CHAMADO_ID IS NULL OR (C.EMPRESA_ID=?"
                                                        + (util.Seguranca.equipe(usuario)
                                                                ? ""
                                                                : " AND C.SOLICITANTE_ID=?")
                                                        + "))")
                                        + " ORDER BY N.ID DESC")) {
            ps.setInt(1, usuario.getId());
            if (!util.Seguranca.master(usuario)) {
                ps.setInt(2, usuario.getEmpresaId());
                if (!util.Seguranca.equipe(usuario)) ps.setInt(3, usuario.getId());
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notificacao n = new Notificacao();
                    n.setId(rs.getInt("ID"));
                    n.setChamadoId(rs.getInt("CHAMADO_ID"));
                    n.setTexto(rs.getString("TEXTO"));
                    n.setLida(rs.getBoolean("LIDA"));
                    n.setCriadoEm(rs.getTimestamp("CRIADO_EM"));
                    lista.add(n);
                }
            }
            return lista;
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível consultar notificações.", e);
        }
    }

    public void lerTodas(int usuarioId) {
        try (Connection c = Banco.conectar();
                PreparedStatement ps =
                        c.prepareStatement("UPDATE NOTIFICACAO SET LIDA=TRUE WHERE USUARIO_ID=?")) {
            ps.setInt(1, usuarioId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível atualizar notificações.", e);
        }
    }

    public void ler(int id, int usuarioId) {
        try (Connection c = Banco.conectar();
                PreparedStatement ps =
                        c.prepareStatement(
                                "UPDATE NOTIFICACAO SET LIDA=TRUE WHERE ID=? AND USUARIO_ID=?")) {
            ps.setInt(1, id);
            ps.setInt(2, usuarioId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível atualizar a notificação.", e);
        }
    }
}
