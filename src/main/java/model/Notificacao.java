package model;

public class Notificacao {
    private int id;
    private int chamadoId;
    private String texto;
    private boolean lida;
    private java.sql.Timestamp criadoEm;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChamadoId() {
        return chamadoId;
    }

    public void setChamadoId(int chamadoId) {
        this.chamadoId = chamadoId;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public java.sql.Timestamp getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(java.sql.Timestamp criadoEm) {
        this.criadoEm = criadoEm;
    }
}
