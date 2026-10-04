package dao;

import model.Colaborador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ColaboradorDAO {

    public void createColaborador(Colaborador colaborador) {

        String SQL = "INSERT INTO COLABORADOR (NOME,EMAIL,TELEFONE,ID_EMPRESA) VALUES (?,?,?,?)";

        try {

            Connection conectar = DriverManager.getConnection("jdbc:h2:~/test", "admin", "admin123");
            System.out.println("Sucesso ao se conectar!");

            PreparedStatement preparedStatement = conectar.prepareStatement(SQL);

            preparedStatement.setString(1, colaborador.getNome());
            preparedStatement.setString(2, colaborador.getEmail());
            preparedStatement.setString(3, colaborador.getTelefone());
            preparedStatement.setInt(4, colaborador.getIdDaEmpresa());

            preparedStatement.execute();
            System.out.println("Sucesso ao inserir!");

            conectar.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}