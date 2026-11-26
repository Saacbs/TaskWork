package util;

import javax.servlet.http.HttpServletRequest;

public class Validacao {
    public static String texto(
            HttpServletRequest pedido, String campo, int limite, boolean obrigatorio) {
        String valor = pedido.getParameter(campo);
        valor = valor == null ? "" : valor.trim();
        if ((obrigatorio && valor.isEmpty()) || valor.length() > limite)
            throw new IllegalArgumentException(
                    "Preencha corretamente " + campo + " (até " + limite + " caracteres).");
        return valor;
    }

    public static String email(HttpServletRequest pedido) {
        String email = texto(pedido, "email", 160, true).toLowerCase(java.util.Locale.ROOT);
        if (!email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"))
            throw new IllegalArgumentException("E-mail inválido.");
        return email;
    }

    public static int inteiro(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int protocolo(String valor) {
        if (valor == null || !valor.matches("(?i)(CH-(?:[0-9]{4}-)?)?[0-9]+")) return 0;
        return inteiro(valor.substring(valor.lastIndexOf('-') + 1));
    }
}
