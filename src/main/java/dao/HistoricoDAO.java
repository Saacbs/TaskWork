package dao;

import model.Historico;

import util.Banco;

import java.sql.*;
import java.util.*;

public class HistoricoDAO {
    public void adicionar(Connection c, int chamadoId, int autorId, String texto, boolean interno)
            throws SQLException {
        try (PreparedStatement ps =
                c.prepareStatement(
                        "INSERT INTO HISTORICO (CHAMADO_ID,AUTOR_ID,TEXTO,INTERNO) VALUES"
                                + " (?,?,?,?)")) {
            ps.setInt(1, chamadoId);
            ps.setInt(2, autorId);
            ps.setString(3, texto);
            ps.setBoolean(4, interno);
            ps.executeUpdate();
        }
    }

    public List<Historico> listar(int chamadoId, boolean equipe) {
        List<Historico> lista = new ArrayList<>();
        try (Connection c = Banco.conectar();
                PreparedStatement ps =
                        c.prepareStatement(
                                "SELECT H.*,U.NOME AUTOR FROM HISTORICO H JOIN USUARIO U ON"
                                    + " U.ID=H.AUTOR_ID WHERE CHAMADO_ID=? AND (INTERNO=FALSE OR"
                                    + " ?=TRUE) ORDER BY H.ID")) {
            ps.setInt(1, chamadoId);
            ps.setBoolean(2, equipe);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Historico h = new Historico();
                    h.setAutor(rs.getString("AUTOR"));
                    h.setInterno(rs.getBoolean("INTERNO"));
                    h.setTexto(rs.getString("TEXTO"));
                    h.setCriadoEm(rs.getTimestamp("CRIADO_EM"));
                    lista.add(h);
                }
            }
            return lista;
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível consultar o histórico.", e);
        }
    }
}
