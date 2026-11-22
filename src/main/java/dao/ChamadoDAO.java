package dao;

import model.*;

import util.*;

import java.sql.*;
import java.util.*;

public class ChamadoDAO {
    private static final String CONSULTA =
            "SELECT C.*, E.NOME EMPRESA_NOME, U.NOME SOLICITANTE_NOME, R.NOME RESPONSAVEL_NOME,"
                + " K.NOME CATEGORIA_NOME FROM CHAMADO C LEFT JOIN EMPRESA E ON E.ID=C.EMPRESA_ID"
                + " LEFT JOIN USUARIO U ON U.ID=C.SOLICITANTE_ID LEFT JOIN USUARIO R ON"
                + " R.ID=C.RESPONSAVEL_ID LEFT JOIN CATEGORIA K ON K.ID=C.CATEGORIA_ID";

    public List<Chamado> listar(Usuario ator) {
        String filtro =
                Seguranca.master(ator)
                        ? ""
                        : " WHERE C.EMPRESA_ID=?"
                                + (Seguranca.equipe(ator) ? "" : " AND C.SOLICITANTE_ID=?");
        List<Chamado> lista = new ArrayList<>();
        try (Connection c = Banco.conectar();
                PreparedStatement ps =
                        c.prepareStatement(CONSULTA + filtro + " ORDER BY C.ID DESC")) {
            if (!Seguranca.master(ator)) {
                ps.setInt(1, ator.getEmpresaId());
                if (!Seguranca.equipe(ator)) ps.setInt(2, ator.getId());
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(montar(rs));
            }
            return lista;
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível consultar chamados.", e);
        }
    }

    public Chamado buscarPorId(int id, Usuario ator) {
        try (Connection c = Banco.conectar();
                PreparedStatement ps = c.prepareStatement(CONSULTA + " WHERE C.ID=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                Chamado chamado = rs.next() ? montar(rs) : null;
                return Seguranca.podeVer(ator, chamado) ? chamado : null;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível consultar o chamado.", e);
        }
    }

    public void salvar(
            Chamado chamado,
            Usuario ator,
            List<Anexo> anexos,
            String evento,
            String statusAnterior) {
        salvar(chamado, ator, anexos, evento, statusAnterior, false);
    }

    public void salvar(
            Chamado chamado,
            Usuario ator,
            List<Anexo> anexos,
            String evento,
            String statusAnterior,
            boolean interno) {
        boolean novo = chamado.getId() == 0;
        chamado.setAtualizadoEm(new Timestamp(System.currentTimeMillis()));
        try (Connection c = Banco.conectar()) {
            c.setAutoCommit(false);
            try {
                if (!novo) {
                    try (PreparedStatement trava =
                            c.prepareStatement(
                                    "SELECT STATUS FROM CHAMADO WHERE ID=? FOR UPDATE")) {
                        trava.setInt(1, chamado.getId());
                        try (ResultSet rs = trava.executeQuery()) {
                            if (!rs.next() || !rs.getString(1).equals(statusAnterior))
                                throw new IllegalArgumentException(
                                        "O chamado foi atualizado. Recarregue a página.");
                        }
                    }
                }
                String sql =
                        novo
                                ? "INSERT INTO CHAMADO"
                                      + " (TITULO,DESCRICAO,SETOR,CATEGORIA,PRIORIDADE,STATUS,EMPRESA_ID,SOLICITANTE_ID,RESPONSAVEL_ID,CATEGORIA_ID,CRIADO_EM,RESOLVIDO_EM,SOLUCAO,AVALIACAO,COMENTARIO_AVALIACAO,PRAZO_SLA,ATUALIZADO_EM)"
                                      + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                                : "UPDATE CHAMADO SET"
                                      + " TITULO=?,DESCRICAO=?,SETOR=?,CATEGORIA=?,PRIORIDADE=?,STATUS=?,EMPRESA_ID=?,SOLICITANTE_ID=?,RESPONSAVEL_ID=?,CATEGORIA_ID=?,CRIADO_EM=?,RESOLVIDO_EM=?,SOLUCAO=?,AVALIACAO=?,COMENTARIO_AVALIACAO=?,PRAZO_SLA=?,ATUALIZADO_EM=?"
                                      + " WHERE ID=?";
                try (PreparedStatement ps =
                        c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, chamado.getTitulo());
                    ps.setString(2, chamado.getDescricao());
                    ps.setString(3, chamado.getSetor());
                    ps.setString(4, chamado.getCategoria());
                    ps.setString(5, chamado.getPrioridade());
                    ps.setString(6, chamado.getStatus());
                    nullable(ps, 7, chamado.getEmpresaId());
                    nullable(ps, 8, chamado.getSolicitanteId());
                    nullable(ps, 9, chamado.getResponsavelId());
                    nullable(ps, 10, chamado.getCategoriaId());
                    ps.setTimestamp(11, chamado.getCriadoEm());
                    ps.setTimestamp(12, chamado.getResolvidoEm());
                    ps.setString(13, chamado.getSolucao());
                    nullable(ps, 14, chamado.getAvaliacao());
                    ps.setString(15, chamado.getComentarioAvaliacao());
                    ps.setTimestamp(16, chamado.getPrazoSla());
                    ps.setTimestamp(17, chamado.getAtualizadoEm());
                    if (!novo) ps.setInt(18, chamado.getId());
                    ps.executeUpdate();
                    if (novo)
                        try (ResultSet rs = ps.getGeneratedKeys()) {
                            rs.next();
                            chamado.setId(rs.getInt(1));
                        }
                }
                if (novo && chamado.getCriadoEm() != null) {
                    chamado.setProtocolo(
                            String.format(
                                    java.util.Locale.ROOT,
                                    "CH-%d-%06d",
                                    chamado.getCriadoEm().toLocalDateTime().getYear(),
                                    chamado.getId()));
                    try (PreparedStatement ps =
                            c.prepareStatement("UPDATE CHAMADO SET PROTOCOLO=? WHERE ID=?")) {
                        ps.setString(1, chamado.getProtocolo());
                        ps.setInt(2, chamado.getId());
                        ps.executeUpdate();
                    }
                }
                new AnexoDAO().salvar(c, chamado.getId(), ator.getId(), anexos);
                new HistoricoDAO().adicionar(c, chamado.getId(), ator.getId(), evento, interno);
                new NotificacaoDAO()
                        .avisar(
                                c,
                                chamado,
                                ator.getId(),
                                novo
                                        ? "Novo chamado aberto."
                                        : "Atualização no chamado: " + chamado.getTitulo(),
                                interno);
                c.commit();
            } catch (Exception e) {
                c.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Não foi possível salvar o chamado. Confira os dados e recarregue a página.",
                    e);
        }
    }

    private void nullable(PreparedStatement ps, int pos, int valor) throws SQLException {
        if (valor == 0) ps.setNull(pos, Types.INTEGER);
        else ps.setInt(pos, valor);
    }

    public void excluir(int id) {
        try (Connection c = Banco.conectar();
                PreparedStatement ps = c.prepareStatement("DELETE FROM CHAMADO WHERE ID=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new IllegalArgumentException("Não foi possível excluir o chamado.", e);
        }
    }

    private Chamado montar(ResultSet rs) throws SQLException {
        Chamado c = new Chamado();
        c.setId(rs.getInt("ID"));
        c.setTitulo(rs.getString("TITULO"));
        c.setDescricao(rs.getString("DESCRICAO"));
        c.setSetor(rs.getString("SETOR"));
        c.setCategoria(
                rs.getString("CATEGORIA_NOME") == null
                        ? rs.getString("CATEGORIA")
                        : rs.getString("CATEGORIA_NOME"));
        c.setPrioridade(rs.getString("PRIORIDADE"));
        c.setStatus(rs.getString("STATUS"));
        c.setEmpresaId(rs.getInt("EMPRESA_ID"));
        c.setSolicitanteId(rs.getInt("SOLICITANTE_ID"));
        c.setResponsavelId(rs.getInt("RESPONSAVEL_ID"));
        c.setCategoriaId(rs.getInt("CATEGORIA_ID"));
        c.setEmpresaNome(rs.getString("EMPRESA_NOME"));
        c.setSolicitanteNome(rs.getString("SOLICITANTE_NOME"));
        c.setResponsavelNome(rs.getString("RESPONSAVEL_NOME"));
        c.setCriadoEm(rs.getTimestamp("CRIADO_EM"));
        c.setResolvidoEm(rs.getTimestamp("RESOLVIDO_EM"));
        c.setSolucao(rs.getString("SOLUCAO"));
        c.setAvaliacao(rs.getInt("AVALIACAO"));
        c.setComentarioAvaliacao(rs.getString("COMENTARIO_AVALIACAO"));
        c.setProtocolo(rs.getString("PROTOCOLO"));
        c.setPrazoSla(rs.getTimestamp("PRAZO_SLA"));
        c.setAtualizadoEm(rs.getTimestamp("ATUALIZADO_EM"));
        return c;
    }
}
