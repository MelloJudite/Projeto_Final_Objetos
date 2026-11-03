package br.edu.insper.projeto_final_objetos.auth;

public class LoginResponse {
    private String token;
    private String tipoToken;
    private long expiraEmSegundos;
    private Perfil perfil;

    public LoginResponse() { }

    public LoginResponse(String token, String tipoToken, long expiraEmSegundos, Perfil perfil) {
        this.token = token;
        this.tipoToken = tipoToken;
        this.expiraEmSegundos = expiraEmSegundos;
        this.perfil = perfil;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(String tipoToken) {
        this.tipoToken = tipoToken;
    }

    public long getExpiraEmSegundos() {
        return expiraEmSegundos;
    }

    public void setExpiraEmSegundos(long expiraEmSegundos) {
        this.expiraEmSegundos = expiraEmSegundos;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
