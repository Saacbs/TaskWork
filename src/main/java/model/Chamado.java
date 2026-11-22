package model;

public class Chamado {
    public static final java.util.List<String> STATUS =
            java.util.List.of(
                    "Aberto",
                    "Em atendimento",
                    "Aguardando contato",
                    "Aguardando aprovação",
                    "Resolvido",
                    "Recusado",
                    "Cancelado");
    private String protocolo;
    private java.sql.Timestamp prazoSla;
    private java.sql.Timestamp atualizadoEm;
    private String comentarioAvaliacao;

    public String getProtocolo() {
        return protocolo == null ? "CH-" + id : protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public java.sql.Timestamp getPrazoSla() {
        return prazoSla;
    }

    public void setPrazoSla(java.sql.Timestamp prazoSla) {
        this.prazoSla = prazoSla;
    }

    public java.sql.Timestamp getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(java.sql.Timestamp atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public String getComentarioAvaliacao() {
        return comentarioAvaliacao;
    }

    public void setComentarioAvaliacao(String comentarioAvaliacao) {
        this.comentarioAvaliacao = comentarioAvaliacao;
    }

    public boolean isEncerrado() {
        return java.util.List.of("Resolvido", "Cancelado", "Recusado").contains(status);
    }

    public boolean isAtrasado() {
        return !isEncerrado()
                && prazoSla != null
                && prazoSla.before(new java.sql.Timestamp(System.currentTimeMillis()));
    }

    public static int horasSla(String prioridade) {
        return switch (prioridade) {
            case "Crítica" -> 4;
            case "Alta" -> 8;
            case "Média" -> 24;
            case "Baixa" -> 48;
            default -> throw new IllegalArgumentException("Prioridade inválida.");
        };
    }

    private int id;
    private String titulo;
    private String descricao;
    private String setor;
    private String categoria;
    private String prioridade = "Média";
    private String status = "Aberto";
    private int empresaId;
    private int solicitanteId;
    private int responsavelId;
    private int categoriaId;
    private String empresaNome;
    private String solicitanteNome;
    private String responsavelNome;
    private java.sql.Timestamp criadoEm;
    private java.sql.Timestamp resolvidoEm;
    private String solucao;
    private int avaliacao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(int empresaId) {
        this.empresaId = empresaId;
    }

    public int getSolicitanteId() {
        return solicitanteId;
    }

    public void setSolicitanteId(int solicitanteId) {
        this.solicitanteId = solicitanteId;
    }

    public int getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(int responsavelId) {
        this.responsavelId = responsavelId;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getEmpresaNome() {
        return empresaNome;
    }

    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
    }

    public String getSolicitanteNome() {
        return solicitanteNome;
    }

    public void setSolicitanteNome(String solicitanteNome) {
        this.solicitanteNome = solicitanteNome;
    }

    public String getResponsavelNome() {
        return responsavelNome;
    }

    public void setResponsavelNome(String responsavelNome) {
        this.responsavelNome = responsavelNome;
    }

    public java.sql.Timestamp getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(java.sql.Timestamp criadoEm) {
        this.criadoEm = criadoEm;
    }

    public java.sql.Timestamp getResolvidoEm() {
        return resolvidoEm;
    }

    public void setResolvidoEm(java.sql.Timestamp resolvidoEm) {
        this.resolvidoEm = resolvidoEm;
    }

    public String getSolucao() {
        return solucao;
    }

    public void setSolucao(String solucao) {
        this.solucao = solucao;
    }

    public int getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }
}
