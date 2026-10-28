package util;

import org.h2.tools.RunScript;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;

public class Banco {
    private static final String URL = "jdbc:h2:./dados/taskwork";

    public static Connection conectar() throws Exception {
        return DriverManager.getConnection(URL, "sa", "");
    }

    public static void inicializar() {
        try (Connection conexao = conectar()) {
            RunScript.execute(conexao, new InputStreamReader(
                    Banco.class.getResourceAsStream("/database.sql"), StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível iniciar o banco de dados.", e);
        }
    }
}
