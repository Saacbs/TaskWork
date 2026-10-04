package br.com.taskwork;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

public class IniciadorBanco {

    public static void initialize() {

        try {

            InputStream arquivo = IniciadorBanco.class
                    .getClassLoader()
                    .getResourceAsStream("database.sql");

            if (arquivo == null) {
                throw new RuntimeException("Arquivo database.sql não encontrado!");
            }

            Scanner scanner = new Scanner(
                    arquivo,
                    StandardCharsets.UTF_8.name()
            ).useDelimiter("\\A");

            String sql = scanner.hasNext() ? scanner.next() : "";

            scanner.close();

            Connection conectar = DriverManager.getConnection(
                    "jdbc:h2:~/test",
                    "admin",
                    "admin123"
            );

            Statement statement = conectar.createStatement();

            for (String comando : sql.split(";")) {

                if (!comando.trim().isEmpty()) {
                    statement.execute(comando);
                }

            }

            statement.close();
            conectar.close();

            System.out.println("Banco inicializado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}