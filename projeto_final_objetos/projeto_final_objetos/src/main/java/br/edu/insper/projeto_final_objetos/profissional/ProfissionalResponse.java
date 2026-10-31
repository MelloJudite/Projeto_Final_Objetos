package br.edu.insper.projeto_final_objetos.profissional;

public class ProfissionalResponse {
    private Integer id;
    private String nome;
    private String telefone;
    private String email;
    private String tipoServico;

    public ProfissionalResponse() { }

    public ProfissionalResponse(Integer id, String nome, String telefone, String email, String tipoServico) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.tipoServico = tipoServico;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public static ProfissionalResponse de(Profissional valor) {
        return new ProfissionalResponse(valor.getId(), valor.getNome(), valor.getTelefone(), valor.getEmail(), valor.getTipoServico());
    }
}
