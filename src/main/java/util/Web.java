package util;

public class Web {
    public static String html(String texto) {
        if (texto == null) return "";
        return texto.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    public static String selecionado(Object atual, Object opcao) {
        return atual != null && atual.toString().equals(opcao.toString()) ? "selected" : "";
    }

    public static String data(java.sql.Timestamp data) {
        return data == null
                ? "Não informada"
                : data.toLocalDateTime()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
