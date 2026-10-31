package br.edu.insper.projeto_final_objetos.auth;

// O principal identifica a conta pelo ID e pelo tipo, sem armazenar a senha.
public class UsuarioAutenticado {
    private final Integer id;
    private final Perfil perfil;

    public UsuarioAutenticado(Integer id, Perfil perfil) {
        this.id = id;
        this.perfil = perfil;
    }

    public Integer getId() {
        return id;
    }

    public Perfil getPerfil() {
        return perfil;
    }
}
