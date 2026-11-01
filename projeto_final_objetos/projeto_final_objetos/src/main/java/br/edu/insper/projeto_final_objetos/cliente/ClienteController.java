package br.edu.insper.projeto_final_objetos.cliente;

import br.edu.insper.projeto_final_objetos.auth.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService service;
    private final AutenticacaoService autenticacaoService;

    public ClienteController(ClienteService service, AutenticacaoService autenticacaoService) {
        this.service = service;
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse cadastrar(@Valid @RequestBody CadastroClienteRequest request) {
        return service.cadastrar(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return autenticacaoService.loginCliente(request);
    }

    @GetMapping("/me")
    public ClienteResponse minhaConta(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return service.buscarMinhaConta(usuario.getId());
    }
}
