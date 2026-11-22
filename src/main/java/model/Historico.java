package model;

public class Historico {
    private boolean interno;

    public boolean isInterno() {
        return interno;
    }

    public void setInterno(boolean interno) {
        this.interno = interno;
    }

    private String autor;
    private String texto;
    private java.sql.Timestamp criadoEm;

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public java.sql.Timestamp getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(java.sql.Timestamp criadoEm) {
        this.criadoEm = criadoEm;
    }
}
