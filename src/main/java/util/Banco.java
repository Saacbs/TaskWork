package util;

import org.h2.tools.RunScript;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Banco {
    private static final String URL = "jdbc:h2:./dados/taskwork";
    public static final String CHAVE_INICIAL = java.util.UUID.randomUUID().toString();
    // Acesso fixo apenas para demonstração local do projeto acadêmico.
    public static final String MASTER_EMAIL = "master@taskwork.local";
    public static final String MASTER_SENHA = "Master@123";

    public static Connection conectar() throws Exception {
        return DriverManager.getConnection(URL, "sa", "");
    }

    public static void inicializar() {
        try (Connection conexao = conectar()) {
            conexao.setAutoCommit(false);
            RunScript.execute(
                    conexao,
                    new InputStreamReader(
                            Banco.class.getResourceAsStream("/database.sql"),
                            StandardCharsets.UTF_8));
            criarMaster(conexao);
            conexao.commit();
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível iniciar o banco de dados.", e);
        }
    }

    private static void criarMaster(Connection conexao) throws Exception {
        try (PreparedStatement consulta =
                conexao.prepareStatement(
                        "SELECT PERFIL FROM USUARIO WHERE LOWER(EMAIL)=LOWER(?)")) {
            consulta.setString(1, MASTER_EMAIL);
            try (ResultSet resultado = consulta.executeQuery()) {
                if (resultado.next()) {
                    if (!"Administrador master".equals(resultado.getString("PERFIL")))
                        throw new IllegalStateException(
                                "O e-mail do master fixo já pertence a outro perfil."
                                        + " Altere MASTER_EMAIL em util/Banco.java.");
                    return;
                }
            }
        }
        try (PreparedStatement comando =
                conexao.prepareStatement(
                        "INSERT INTO USUARIO (NOME,EMAIL,EMPRESA_ID,PERFIL,ATIVO,SENHA)"
                                + " VALUES (?, ?, NULL, 'Administrador master', TRUE, ?)")) {
            comando.setString(1, "Administrador master");
            comando.setString(2, MASTER_EMAIL);
            comando.setString(3, Senha.gerar(MASTER_SENHA));
            comando.executeUpdate();
        }
    }
}
