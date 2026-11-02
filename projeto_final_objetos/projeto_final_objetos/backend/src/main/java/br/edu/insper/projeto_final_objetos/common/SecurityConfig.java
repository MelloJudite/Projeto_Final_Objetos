
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
            AutenticacaoService autenticacaoService
    ) throws Exception {

        var filtro = new JwtAuthenticationFilter(
                jwtService,
                autenticacaoService
        );

        return http
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

                        // Clientes podem consultar serviços
                        .requestMatchers(
                                HttpMethod.GET,
                                "/servicos",
                                "/servicos/**"
                        ).authenticated()

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

                        .requestMatchers("/agendamentos", "/agendamentos/**")
                        .hasRole("CLIENTE")

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
}
