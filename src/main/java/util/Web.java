package util;

public class Web {
    public static String html(String texto) {
        if (texto == null) return "";
        return texto.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }

    public static String selecionado(Object atual, Object opcao) {
        return atual != null && atual.toString().equals(opcao.toString()) ? "selected" : "";
    }
}
