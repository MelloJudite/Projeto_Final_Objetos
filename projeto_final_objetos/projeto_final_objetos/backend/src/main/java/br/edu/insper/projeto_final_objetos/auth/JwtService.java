package br.edu.insper.projeto_final_objetos.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey chave;
    private final long expiracao;
    private final String emissor;

    public JwtService(@Value("${jwt.secret}") String segredo,
                      @Value("${jwt.expiration:3600000}") long expiracao,
                      @Value("${spring.application.name}") String emissor) {
        if (expiracao <= 0) {
            throw new IllegalArgumentException("A duração do token deve ser positiva");
        }
        this.chave = Keys.hmacShaKeyFor(segredo.getBytes(StandardCharsets.UTF_8));
        this.expiracao = expiracao;
        this.emissor = emissor;
    }

    public String gerarToken(Integer id, Perfil perfil) {
        Date agora = new Date();
        return Jwts.builder()
                .issuer(emissor)
                .subject(id.toString())
                .claim("perfil", perfil.name())
                .issuedAt(agora)
                .expiration(new Date(agora.getTime() + expiracao))
                .signWith(chave, Jwts.SIG.HS256)
                .compact();
    }

    public UsuarioAutenticado validarToken(String token) {
        Claims claims = Jwts.parser().verifyWith(chave).requireIssuer(emissor)
                .build().parseSignedClaims(token).getPayload();
        if (claims.getExpiration() == null) {
            throw new JwtException("Token sem expiração");
        }
        Integer id = Integer.valueOf(claims.getSubject());
        String valorPerfil = claims.get("perfil", String.class);
        if (valorPerfil == null) {
            throw new JwtException("Token sem perfil");
        }
        Perfil perfil = Perfil.valueOf(valorPerfil);
        if (id <= 0) {
            throw new JwtException("Identificação inválida");
        }
        return new UsuarioAutenticado(id, perfil);
    }

    public long getExpiracaoEmSegundos() {
        return expiracao / 1000;
    }
}
