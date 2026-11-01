package br.edu.insper.projeto_final_objetos.profissional;

import jakarta.validation.constraints.*;

public class CadastroProfissionalRequest {
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "\\+?[0-9]{10,13}", message = "Informe o telefone com 10 a 13 dígitos, com + opcional, sem espaços ou pontuação")
    private String telefone;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    @Size(max = 254, message = "O email deve ter no máximo 254 caracteres")
    private String email;

    @NotBlank(message = "O tipo de serviço é obrigatório")
    @Size(max = 100, message = "O tipo de serviço deve ter no máximo 100 caracteres")
    private String tipoServico;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, max = 72, message = "A senha deve ter de 8 a 72 caracteres e no máximo 72 bytes UTF-8")
    private String senha;

    public CadastroProfissionalRequest() { }

    public CadastroProfissionalRequest(String nome, String telefone, String email, String tipoServico, String senha) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.tipoServico = tipoServico;
        this.senha = senha;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
