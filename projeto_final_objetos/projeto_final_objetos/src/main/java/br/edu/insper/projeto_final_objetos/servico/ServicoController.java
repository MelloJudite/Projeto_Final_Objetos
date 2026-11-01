package br.edu.insper.projeto_final_objetos.servico;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Servico cadastrar(
            @Valid @RequestBody CriarServicoRequest request
    ) {

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

        return servicoService.salvar(servico);
    }

    @GetMapping
    public List<Servico> listar(
            @RequestParam(required = false) String tipo
    ) {
        if (tipo != null) {
            return servicoService.listarPorTipo(tipo.trim());
        }

        return servicoService.listar();
    }

    @GetMapping("/{id}")
    public Servico buscarPorId(@PathVariable int id) {
        return servicoService.buscarPorId(id);
    }
}