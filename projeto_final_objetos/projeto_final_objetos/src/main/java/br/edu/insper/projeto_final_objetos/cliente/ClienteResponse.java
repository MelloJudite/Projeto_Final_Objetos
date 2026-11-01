package br.edu.insper.projeto_final_objetos.cliente;

public class ClienteResponse {
    private Integer id;
    private String nome;
    private String telefone;
    private String email;

    public ClienteResponse() { }

    public ClienteResponse(Integer id, String nome, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
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

    public static ClienteResponse de(Cliente valor) {
        return new ClienteResponse(valor.getId(), valor.getNome(), valor.getTelefone(), valor.getEmail());
    }
}
