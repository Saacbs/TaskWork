package util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Senha {
    public static String gerar(String senha) {
        if (senha == null || senha.length() < 8 || senha.length() > 100)
            throw new IllegalArgumentException("A senha deve ter de 8 a 100 caracteres.");
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt)
                + ":"
                + Base64.getEncoder().encodeToString(calcular(senha, salt));
    }

    public static boolean conferir(String senha, String gravada) {
        if (senha == null || senha.length() > 100 || gravada == null) return false;
        try {
            String[] partes = gravada.split(":");
            return partes.length == 2
                    && MessageDigest.isEqual(
                            Base64.getDecoder().decode(partes[1]),
                            calcular(senha, Base64.getDecoder().decode(partes[0])));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] calcular(String senha, byte[] salt) {
        PBEKeySpec chave = new PBEKeySpec(senha.toCharArray(), salt, 210000, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(chave)
                    .getEncoded();
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível proteger a senha.", e);
        } finally {
            chave.clearPassword();
        }
    }
}
