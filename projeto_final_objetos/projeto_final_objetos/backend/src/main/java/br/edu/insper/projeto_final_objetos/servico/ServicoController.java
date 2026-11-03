
package br.edu.insper.projeto_final_objetos.servico;

import br.edu.insper.projeto_final_objetos.auth.UsuarioAutenticado;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Servico cadastrar(
            @Valid @RequestBody CriarServicoRequest request,
            @AuthenticationPrincipal UsuarioAutenticado usuario
    ) {
        if (usuario == null ||
                usuario.getPerfil().name().equals("CLIENTE")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Somente profissionais podem cadastrar serviços"
            );
        }

        Servico servico = switch (request.tipo().trim().toUpperCase()) {
            case "CABELO" -> new Cabelo();
            case "PELE" -> new Pele();
            case "SOBRANCELHA" -> new Sobrancelha();
            case "UNHA" -> new Unha();
            case "MASSAGEM" -> new Massagem();
            case "MAQUIAGEM" -> new Maquiagem();
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo de serviço inválido"
            );
        };

        servico.setNome(request.nome());
        servico.setDescricao(request.descricao());
        servico.setPreco(request.preco());

        return servicoService.salvar(servico, usuario.getId());
    }

    @GetMapping
    public List<Servico> listar(
            @RequestParam(required = false) String tipo
    ) {
        return tipo == null
                ? servicoService.listar()
                : servicoService.listarPorTipo(tipo);
    }

    @GetMapping("/{id}")
    public Servico buscarPorId(@PathVariable int id) {
        return servicoService.buscarPorId(id);
    }
}
