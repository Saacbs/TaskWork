package dao;

import model.Usuario;
import util.Banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    public void salvar(Usuario usuario) {
        if (emailExiste(usuario.getEmail(), usuario.getId())) {
            throw new IllegalArgumentException("Já existe um usuário com este e-mail.");
        }
        String sql = usuario.getId() == 0
                ? "INSERT INTO USUARIO (NOME, EMAIL, TELEFONE, EMPRESA_ID, PERFIL, ATIVO) VALUES (?, ?, ?, ?, ?, ?)"
                : "UPDATE USUARIO SET NOME=?, EMAIL=?, TELEFONE=?, EMPRESA_ID=?, PERFIL=?, ATIVO=? WHERE ID=?";
        try (Connection conexao = Banco.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {
            preencher(comando, usuario);
            if (usuario.getId() > 0) comando.setInt(7, usuario.getId());
            comando.executeUpdate();
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível salvar o usuário.", e);
        }
    }

    public List<Usuario> listar() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT U.*, E.NOME AS EMPRESA_NOME FROM USUARIO U JOIN EMPRESA E ON E.ID = U.EMPRESA_ID ORDER BY U.NOME";
        try (Connection conexao = Banco.conectar(); PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) usuarios.add(montar(resultado));
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar os usuários.", e);
        }
        return usuarios;
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT U.*, E.NOME AS EMPRESA_NOME FROM USUARIO U JOIN EMPRESA E ON E.ID = U.EMPRESA_ID WHERE U.ID = ?";
        try (Connection conexao = Banco.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, id);
            ResultSet resultado = comando.executeQuery();
            return resultado.next() ? montar(resultado) : null;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível buscar o usuário.", e);
        }
    }

    public void excluir(int id) {
        try (Connection conexao = Banco.conectar();
             PreparedStatement comando = conexao.prepareStatement("DELETE FROM USUARIO WHERE ID = ?")) {
            comando.setInt(1, id);
            comando.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir o usuário.", e);
        }
    }

    private boolean emailExiste(String email, int id) {
        try (Connection conexao = Banco.conectar();
             PreparedStatement comando = conexao.prepareStatement("SELECT ID FROM USUARIO WHERE EMAIL = ? AND ID <> ?")) {
            comando.setString(1, email);
            comando.setInt(2, id);
            return comando.executeQuery().next();
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível validar o e-mail.", e);
        }
    }

    private void preencher(PreparedStatement comando, Usuario usuario) throws Exception {
        comando.setString(1, usuario.getNome());
        comando.setString(2, usuario.getEmail());
        comando.setString(3, usuario.getTelefone());
        comando.setInt(4, usuario.getEmpresaId());
        comando.setString(5, usuario.getPerfil());
        comando.setBoolean(6, usuario.isAtivo());
    }

    private Usuario montar(ResultSet resultado) throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(resultado.getInt("ID"));
        usuario.setNome(resultado.getString("NOME"));
        usuario.setEmail(resultado.getString("EMAIL"));
        usuario.setTelefone(resultado.getString("TELEFONE"));
        usuario.setEmpresaId(resultado.getInt("EMPRESA_ID"));
        usuario.setEmpresaNome(resultado.getString("EMPRESA_NOME"));
        usuario.setPerfil(resultado.getString("PERFIL"));
        usuario.setAtivo(resultado.getBoolean("ATIVO"));
        return usuario;
    }
}
