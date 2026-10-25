
package dao;

import model.Colaborador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ColaboradorDAO {

    public void createColaborador(Colaborador colaborador) {

        String SQL = "INSERT INTO COLABORADOR (NOME, EMAIL, TELEFONE, ID_EMPRESA) VALUES (?, ?, ?, ?)";

        try {
            Connection conectar = DriverManager.getConnection(
                    "jdbc:h2:~/test", "admin", "admin123"
            );

            PreparedStatement preparedStatement = conectar.prepareStatement(SQL);

            preparedStatement.setString(1, colaborador.getNome());
            preparedStatement.setString(2, colaborador.getEmail());
            preparedStatement.setString(3, colaborador.getTelefone());
            preparedStatement.setInt(4, colaborador.getIdDaEmpresa());

            preparedStatement.executeUpdate();

            conectar.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Colaborador> listarColaboradores() {

        String SQL = "SELECT COLABORADOR.NOME, COLABORADOR.EMAIL, " +
                "COLABORADOR.TELEFONE, COLABORADOR.ID_EMPRESA, " +
                "EMPRESA.NOME AS NOME_EMPRESA " +
                "FROM COLABORADOR " +
                "JOIN EMPRESA ON COLABORADOR.ID_EMPRESA = EMPRESA.ID_EMPRESA";

        List<Colaborador> colaboradores = new ArrayList<>();

        try {
            Connection conectar = DriverManager.getConnection(
                    "jdbc:h2:~/test", "admin", "admin123"
            );

            PreparedStatement preparedStatement = conectar.prepareStatement(SQL);
            ResultSet resultado = preparedStatement.executeQuery();

            while (resultado.next()) {
                Colaborador colaborador = new Colaborador();

                colaborador.setNome(resultado.getString("NOME"));
                colaborador.setEmail(resultado.getString("EMAIL"));
                colaborador.setTelefone(resultado.getString("TELEFONE"));
                colaborador.setIdDaEmpresa(resultado.getInt("ID_EMPRESA"));
                colaborador.setNomeEmpresa(resultado.getString("NOME_EMPRESA"));

                colaboradores.add(colaborador);
            }

            conectar.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return colaboradores;
    }
}
