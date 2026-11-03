package br.edu.insper.projeto_final_objetos.agendamento;

import br.edu.insper.projeto_final_objetos.auth.UsuarioAutenticado;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {
    private final AgendamentoService service;
    public AgendamentoController(AgendamentoService service) { this.service = service; }

    @GetMapping("/disponibilidade")
    public List<String> disponibilidade(@RequestParam Integer servicoId, @RequestParam LocalDate data) {
        return service.horariosDisponiveis(servicoId, data);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgendamentoResponse reservar(@Valid @RequestBody CriarAgendamentoRequest request,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return AgendamentoResponse.from(service.reservar(request, usuario));
    }

    @GetMapping
    public List<AgendamentoResponse> listarCliente(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return service.listarCliente(usuario.getId());
    }

    @GetMapping("/solicitacoes")
    public List<AgendamentoResponse> solicitacoes(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return service.listarSolicitacoes(usuario.getId());
    }

    @GetMapping("/minha-agenda")
    public List<AgendamentoResponse> minhaAgenda(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return service.listarAgenda(usuario.getId());
    }

    @PostMapping("/{id}/aceitar")
    public AgendamentoResponse aceitar(@PathVariable Integer id,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return service.aceitar(id, usuario.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelarCliente(@PathVariable int id,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {
        service.cancelarCliente(id, usuario.getId());
    }

    @DeleteMapping("/{id}/profissional")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelarProfissional(@PathVariable int id,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {
        service.cancelarProfissional(id, usuario.getId());
    }
}
