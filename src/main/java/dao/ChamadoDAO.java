package dao;

import model.Chamado;
import util.Banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChamadoDAO {
    public void salvar(Chamado chamado) {
        String sql = chamado.getId() == 0
                ? "INSERT INTO CHAMADO (TITULO, DESCRICAO, SETOR, CATEGORIA, PRIORIDADE, STATUS) VALUES (?, ?, ?, ?, ?, ?)"
                : "UPDATE CHAMADO SET TITULO=?, DESCRICAO=?, SETOR=?, CATEGORIA=?, PRIORIDADE=?, STATUS=? WHERE ID=?";
        try (Connection conexao = Banco.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, chamado.getTitulo());
            comando.setString(2, chamado.getDescricao());
            comando.setString(3, chamado.getSetor());
            comando.setString(4, chamado.getCategoria());
            comando.setString(5, chamado.getPrioridade());
            comando.setString(6, chamado.getStatus());
            if (chamado.getId() > 0) comando.setInt(7, chamado.getId());
            comando.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível salvar o chamado.", e);
        }
    }

    public List<Chamado> listar() {
        List<Chamado> chamados = new ArrayList<>();
        try (Connection conexao = Banco.conectar();
             PreparedStatement comando = conexao.prepareStatement("SELECT * FROM CHAMADO ORDER BY ID DESC");
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) chamados.add(montar(resultado));
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar os chamados.", e);
        }
        return chamados;
    }

    public Chamado buscarPorId(int id) {
        try (Connection conexao = Banco.conectar();
             PreparedStatement comando = conexao.prepareStatement("SELECT * FROM CHAMADO WHERE ID = ?")) {
            comando.setInt(1, id);
            ResultSet resultado = comando.executeQuery();
            return resultado.next() ? montar(resultado) : null;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível buscar o chamado.", e);
        }
    }

    public void excluir(int id) {
        try (Connection conexao = Banco.conectar();
             PreparedStatement comando = conexao.prepareStatement("DELETE FROM CHAMADO WHERE ID = ?")) {
            comando.setInt(1, id);
            comando.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir o chamado.", e);
        }
    }

    private Chamado montar(ResultSet resultado) throws Exception {
        Chamado chamado = new Chamado();
        chamado.setId(resultado.getInt("ID"));
        chamado.setTitulo(resultado.getString("TITULO"));
        chamado.setDescricao(resultado.getString("DESCRICAO"));
        chamado.setSetor(resultado.getString("SETOR"));
        chamado.setCategoria(resultado.getString("CATEGORIA"));
        chamado.setPrioridade(resultado.getString("PRIORIDADE"));
        chamado.setStatus(resultado.getString("STATUS"));
        return chamado;
    }
}
