package br.edu.insper.projeto_final_objetos.agendamento;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgendamentoResponse adicionarAoCarrinho(
            @Valid @RequestBody CriarAgendamentoRequest request
    ) {
        return AgendamentoResponse.from(
                agendamentoService.adicionarAoCarrinho(request)
        );
    }

    @GetMapping
    public List<AgendamentoResponse> listar(
            @RequestParam Integer clienteId,
            @RequestParam boolean confirmado
    ) {
        return agendamentoService.listar(clienteId, confirmado)
                .stream()
                .map(AgendamentoResponse::from)
                .toList();
    }

    @PatchMapping("/{id}/confirmar")
    public AgendamentoResponse confirmar(@PathVariable int id) {
        return AgendamentoResponse.from(agendamentoService.confirmar(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable int id) {
        agendamentoService.cancelar(id);
    }
}