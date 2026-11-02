package br.edu.insper.projeto_final_objetos.profissional;

import br.edu.insper.projeto_final_objetos.auth.DadosConta;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProfissionalService {
    private final ProfissionalRepository repository;
    private final PasswordEncoder passwordEncoder;

    public ProfissionalService(ProfissionalRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public ProfissionalResponse cadastrar(CadastroProfissionalRequest request) {
        DadosConta.validarSenha(request.getSenha());
        String email = DadosConta.normalizarEmail(request.getEmail());
        if (repository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado como profissional");
        }
        Profissional conta = new Profissional(request.getNome().trim(), request.getTelefone(), email,
                passwordEncoder.encode(request.getSenha()), request.getTipoServico());
        try {
            return ProfissionalResponse.de(repository.saveAndFlush(conta));
        } catch (DataIntegrityViolationException ex) {
            // A constraint unique também protege contra dois cadastros simultâneos.
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não foi possível cadastrar: email já utilizado ou dados incompatíveis");
        }
    }

    public ProfissionalResponse buscarMinhaConta(Integer id) {
        return repository.findById(id).map(ProfissionalResponse::de)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }
}
