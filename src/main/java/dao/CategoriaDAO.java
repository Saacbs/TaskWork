package dao;

import model.Categoria;
import util.Banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    public void salvar(Categoria categoria) {
        String sql = categoria.getId() == 0
                ? "INSERT INTO CATEGORIA (NOME, DESCRICAO, ATIVO) VALUES (?, ?, ?)"
                : "UPDATE CATEGORIA SET NOME=?, DESCRICAO=?, ATIVO=? WHERE ID=?";
        try (Connection conexao = Banco.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, categoria.getNome());
            comando.setString(2, categoria.getDescricao());
            comando.setBoolean(3, categoria.isAtivo());
            if (categoria.getId() > 0) comando.setInt(4, categoria.getId());
            comando.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível salvar a categoria.", e);
        }
    }

    public List<Categoria> listar() {
        List<Categoria> categorias = new ArrayList<>();
        try (Connection conexao = Banco.conectar();
             PreparedStatement comando = conexao.prepareStatement("SELECT * FROM CATEGORIA ORDER BY NOME");
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) categorias.add(montar(resultado));
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar as categorias.", e);
        }
        return categorias;
    }

    public Categoria buscarPorId(int id) {
        try (Connection conexao = Banco.conectar();
             PreparedStatement comando = conexao.prepareStatement("SELECT * FROM CATEGORIA WHERE ID = ?")) {
            comando.setInt(1, id);
            ResultSet resultado = comando.executeQuery();
            return resultado.next() ? montar(resultado) : null;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível buscar a categoria.", e);
        }
    }

    public void excluir(int id) {
        try (Connection conexao = Banco.conectar();
             PreparedStatement comando = conexao.prepareStatement("DELETE FROM CATEGORIA WHERE ID = ?")) {
            comando.setInt(1, id);
            comando.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir a categoria.", e);
        }
    }

    private Categoria montar(ResultSet resultado) throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId(resultado.getInt("ID"));
        categoria.setNome(resultado.getString("NOME"));
        categoria.setDescricao(resultado.getString("DESCRICAO"));
        categoria.setAtivo(resultado.getBoolean("ATIVO"));
        return categoria;
    }
}
