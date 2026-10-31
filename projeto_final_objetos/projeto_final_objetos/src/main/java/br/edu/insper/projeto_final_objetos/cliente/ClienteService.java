package br.edu.insper.projeto_final_objetos.cliente;

import br.edu.insper.projeto_final_objetos.auth.DadosConta;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClienteService {
    private final ClienteRepository repository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public ClienteResponse cadastrar(CadastroClienteRequest request) {
        DadosConta.validarSenha(request.getSenha());
        String email = DadosConta.normalizarEmail(request.getEmail());
        if (repository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado como cliente");
        }
        Cliente conta = new Cliente(request.getNome().trim(), request.getTelefone(), email,
                passwordEncoder.encode(request.getSenha()));
        try {
            return ClienteResponse.de(repository.saveAndFlush(conta));
        } catch (DataIntegrityViolationException ex) {
            // A constraint unique também protege contra dois cadastros simultâneos.
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não foi possível cadastrar: email já utilizado ou dados incompatíveis");
        }
    }

    public ClienteResponse buscarMinhaConta(Integer id) {
        return repository.findById(id).map(ClienteResponse::de)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }
}
