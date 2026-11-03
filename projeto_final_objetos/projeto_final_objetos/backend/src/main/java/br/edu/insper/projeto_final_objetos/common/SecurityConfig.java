
package br.edu.insper.projeto_final_objetos.common;

import br.edu.insper.projeto_final_objetos.auth.AutenticacaoService;
import br.edu.insper.projeto_final_objetos.auth.JwtService;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtService jwtService,
            AutenticacaoService autenticacaoService,
            CorsConfigurationSource corsConfigurationSource
    ) throws Exception {

        var filtro = new JwtAuthenticationFilter(
                jwtService,
                autenticacaoService
        );

        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Permite tratamento de erros
                        .dispatcherTypeMatchers(
                                DispatcherType.ERROR
                        ).permitAll()

                        // Cadastro e login sem autenticação
                        .requestMatchers(
                                HttpMethod.POST,
                                "/clientes",
                                "/profissionais",
                                "/clientes/login",
                                "/profissionais/login"
                        ).permitAll()

                        // Consulta pública de serviços
                        .requestMatchers(
                                HttpMethod.GET,
                                "/servicos",
                                "/servicos/**"
                        ).permitAll()

                        // Profissionais podem cadastrar serviços
                        .requestMatchers(
                                HttpMethod.POST,
                                "/servicos"
                        ).hasRole("PROFISSIONAL")

                        // Endpoints dos clientes
                        .requestMatchers(
                                "/clientes/**"
                        ).hasRole("CLIENTE")

                        // Endpoints dos profissionais
                        .requestMatchers(
                                "/profissionais/**"
                        ).hasRole("PROFISSIONAL")

                        // Solicitações e agenda dos profissionais
                        .requestMatchers("/agendamentos/solicitacoes", "/agendamentos/minha-agenda",
                                "/agendamentos/*/aceitar", "/agendamentos/*/profissional")
                        .hasRole("PROFISSIONAL")

                        // Agendamentos dos clientes
                        .requestMatchers("/agendamentos/disponibilidade", "/agendamentos")
                        .hasRole("CLIENTE")
                        .requestMatchers("/agendamentos/*")
                        .hasRole("CLIENTE")

                        // Bloqueia qualquer outra rota não configurada
                        .anyRequest().denyAll()
                )

                .exceptionHandling(ex -> ex

                        .authenticationEntryPoint(
                                (request, response, exception) ->
                                        RespostaSeguranca.escrever(
                                                response,
                                                401,
                                                "Faça login para acessar o sistema"
                                        )
                        )

                        .accessDeniedHandler(
                                (request, response, exception) ->
                                        RespostaSeguranca.escrever(
                                                response,
                                                403,
                                                "Seu perfil não tem acesso a esta rota"
                                        )
                        )
                )

                .addFilterBefore(
                        filtro,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
