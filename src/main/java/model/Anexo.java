package model;

public class Anexo {
    private int autorId;
    private String autorNome;
    private java.sql.Timestamp criadoEm;

    public int getAutorId() {
        return autorId;
    }

    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    public String getAutorNome() {
        return autorNome;
    }

    public void setAutorNome(String autorNome) {
        this.autorNome = autorNome;
    }

    public java.sql.Timestamp getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(java.sql.Timestamp criadoEm) {
        this.criadoEm = criadoEm;
    }

    private int id;
    private int chamadoId;
    private String nome;
    private String tipo;
    private byte[] conteudo;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public byte[] getConteudo() {
        return conteudo;
    }

    public void setConteudo(byte[] conteudo) {
        this.conteudo = conteudo;
    }
}
