package br.edu.insper.projeto_final_objetos.auth;

import br.edu.insper.projeto_final_objetos.cliente.ClienteRepository;
import br.edu.insper.projeto_final_objetos.profissional.ProfissionalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AutenticacaoService {
    private final ClienteRepository clienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AutenticacaoService(ClienteRepository clienteRepository,
                               ProfissionalRepository profissionalRepository,
                               PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.clienteRepository = clienteRepository;
        this.profissionalRepository = profissionalRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse loginCliente(LoginRequest request) {
        DadosConta.validarSenha(request.getSenha());
        var conta = clienteRepository.findByEmail(DadosConta.normalizarEmail(request.getEmail()))
                .orElseThrow(this::credenciaisInvalidas);
        validarSenha(request.getSenha(), conta.getSenha());
        return criarResposta(conta.getId(), Perfil.CLIENTE);
    }

    public LoginResponse loginProfissional(LoginRequest request) {
        DadosConta.validarSenha(request.getSenha());
        var conta = profissionalRepository.findByEmail(DadosConta.normalizarEmail(request.getEmail()))
                .orElseThrow(this::credenciaisInvalidas);
        validarSenha(request.getSenha(), conta.getSenha());
        return criarResposta(conta.getId(), Perfil.PROFISSIONAL);
    }

    private void validarSenha(String senha, String hash) {
        if (!passwordEncoder.matches(senha, hash)) {
            throw credenciaisInvalidas();
        }
    }

    private LoginResponse criarResposta(Integer id, Perfil perfil) {
        return new LoginResponse(jwtService.gerarToken(id, perfil), "Bearer",
                jwtService.getExpiracaoEmSegundos(), perfil);
    }

    public void verificarContaExiste(UsuarioAutenticado usuario) {
        boolean existe = switch (usuario.getPerfil()) {
            case CLIENTE -> clienteRepository.existsById(usuario.getId());
            case PROFISSIONAL -> profissionalRepository.existsById(usuario.getId());
        };
        if (!existe) {
            throw new UsernameNotFoundException("Conta não encontrada");
        }
    }

    private ResponseStatusException credenciaisInvalidas() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos");
    }
}
