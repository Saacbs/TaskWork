package dao;

import model.Empresa;
import util.Banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO {
    public void salvar(Empresa empresa) {
        String sql = empresa.getId() == 0
                ? "INSERT INTO EMPRESA (NOME, CNPJ, ATIVO) VALUES (?, ?, ?)"
                : "UPDATE EMPRESA SET NOME = ?, CNPJ = ?, ATIVO = ? WHERE ID = ?";
        try (Connection conexao = Banco.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, empresa.getNome());
            comando.setString(2, empresa.getCnpj());
            comando.setBoolean(3, empresa.isAtivo());
            if (empresa.getId() > 0) comando.setInt(4, empresa.getId());
            comando.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível salvar a empresa.", e);
        }
    }

    public List<Empresa> listar() {
        List<Empresa> empresas = new ArrayList<>();
        try (Connection conexao = Banco.conectar();
             PreparedStatement comando = conexao.prepareStatement("SELECT * FROM EMPRESA ORDER BY NOME");
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) empresas.add(montar(resultado));
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível listar as empresas.", e);
        }
        return empresas;
    }

    public Empresa buscarPorId(int id) {
        try (Connection conexao = Banco.conectar();
             PreparedStatement comando = conexao.prepareStatement("SELECT * FROM EMPRESA WHERE ID = ?")) {
            comando.setInt(1, id);
            ResultSet resultado = comando.executeQuery();
            return resultado.next() ? montar(resultado) : null;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível buscar a empresa.", e);
        }
    }

    public boolean excluir(int id) {
        if (temUsuarios(id)) return false;
        try (Connection conexao = Banco.conectar();
             PreparedStatement comando = conexao.prepareStatement("DELETE FROM EMPRESA WHERE ID = ?")) {
            comando.setInt(1, id);
            comando.executeUpdate();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível excluir a empresa.", e);
        }
    }

    private boolean temUsuarios(int empresaId) {
        try (Connection conexao = Banco.conectar();
             PreparedStatement comando = conexao.prepareStatement("SELECT COUNT(*) FROM USUARIO WHERE EMPRESA_ID = ?")) {
            comando.setInt(1, empresaId);
            ResultSet resultado = comando.executeQuery();
            return resultado.next() && resultado.getInt(1) > 0;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível verificar os usuários da empresa.", e);
        }
    }

    private Empresa montar(ResultSet resultado) throws Exception {
        Empresa empresa = new Empresa();
        empresa.setId(resultado.getInt("ID"));
        empresa.setNome(resultado.getString("NOME"));
        empresa.setCnpj(resultado.getString("CNPJ"));
        empresa.setAtivo(resultado.getBoolean("ATIVO"));
        return empresa;
    }
}
