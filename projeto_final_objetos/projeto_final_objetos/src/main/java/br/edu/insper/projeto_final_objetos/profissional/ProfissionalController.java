package br.edu.insper.projeto_final_objetos.profissional;

import br.edu.insper.projeto_final_objetos.auth.AutenticacaoService;
import br.edu.insper.projeto_final_objetos.auth.LoginRequest;
import br.edu.insper.projeto_final_objetos.auth.LoginResponse;
import br.edu.insper.projeto_final_objetos.auth.UsuarioAutenticado;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {
    private final ProfissionalService service;
    private final AutenticacaoService autenticacaoService;

    public ProfissionalController(ProfissionalService service, AutenticacaoService autenticacaoService) {
        this.service = service;
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfissionalResponse cadastrar(@Valid @RequestBody CadastroProfissionalRequest request) {
        return service.cadastrar(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return autenticacaoService.loginProfissional(request);
    }

    @GetMapping("/me")
    public ProfissionalResponse minhaConta(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return service.buscarMinhaConta(usuario.getId());
    }
}
