package model;

public class Colaborador {
    private  String nome;
    private  String email;
    private  String telefone;
    private  int IdDaEmpresa;
    private String nomeEmpresa;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getIdDaEmpresa() {
        return IdDaEmpresa;
    }

    public void setIdDaEmpresa(int idDaEmpresa) {
        IdDaEmpresa = idDaEmpresa;
    }

    public String getNomeEmpresa() {return nomeEmpresa;}

    public void setNomeEmpresa(String nomeEmpresa) {this.nomeEmpresa = nomeEmpresa;}
}
