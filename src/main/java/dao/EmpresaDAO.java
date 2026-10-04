package dao;

import model.Empresa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO {

    public void createEmpresa(Empresa empresa) {

        String SQL = "INSERT INTO EMPRESA (NOME,CNPJ) VALUES (?,?)";

        try {

            Connection conectar = DriverManager.getConnection("jdbc:h2:~/test", "admin", "admin123");
            System.out.println("Sucesso ao se conectar!");

            PreparedStatement preparedStatement = conectar.prepareStatement(SQL);

            preparedStatement.setString(1, empresa.getName());
            preparedStatement.setString(2, empresa.getCnpj());

            preparedStatement.execute();

            conectar.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Empresa> listarEmpresas() {

        String SQL = "SELECT ID_EMPRESA, NOME, CNPJ FROM EMPRESA";

        List<Empresa> empresas = new ArrayList<>();

        try {

            Connection conectar = DriverManager.getConnection("jdbc:h2:~/test", "admin", "admin123");

            PreparedStatement preparedStatement = conectar.prepareStatement(SQL);

            ResultSet resultado = preparedStatement.executeQuery();

            while (resultado.next()) {

                Empresa empresa = new Empresa();

                empresa.setId(resultado.getInt("ID_EMPRESA"));
                empresa.setName(resultado.getString("NOME"));
                empresa.setCnpj(resultado.getString("CNPJ"));

                empresas.add(empresa);
            }

            conectar.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return empresas;
    }
}