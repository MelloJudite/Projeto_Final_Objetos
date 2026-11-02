package br.edu.insper.projeto_final_objetos.common;

import br.edu.insper.projeto_final_objetos.auth.AutenticacaoService;
import br.edu.insper.projeto_final_objetos.auth.JwtService;
import br.edu.insper.projeto_final_objetos.auth.UsuarioAutenticado;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Set<String> ROTAS_PUBLICAS = Set.of(
            "/clientes", "/clientes/login", "/profissionais", "/profissionais/login");
    private final JwtService jwtService;
    private final AutenticacaoService autenticacaoService;

    public JwtAuthenticationFilter(JwtService jwtService, AutenticacaoService autenticacaoService) {
        this.jwtService = jwtService;
        this.autenticacaoService = autenticacaoService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && ROTAS_PUBLICAS.contains(request.getServletPath());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String cabecalho = request.getHeader("Authorization");
        if (cabecalho == null || !cabecalho.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            UsuarioAutenticado usuario = jwtService.validarToken(cabecalho.substring(7));
            autenticacaoService.verificarContaExiste(usuario);
            var permissoes = List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name()));
            var autenticacao = new UsernamePasswordAuthenticationToken(usuario, null, permissoes);
            SecurityContextHolder.getContext().setAuthentication(autenticacao);
        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException ex) {
            SecurityContextHolder.clearContext();
            RespostaSeguranca.escrever(response, 401, "Token inválido ou expirado");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
