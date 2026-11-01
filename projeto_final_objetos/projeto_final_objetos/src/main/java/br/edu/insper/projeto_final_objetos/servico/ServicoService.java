package br.edu.insper.projeto_final_objetos.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public Servico salvar(Servico servico) {

        if (servico.getNome() == null ||
                servico.getNome().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O nome do serviço é obrigatório"
            );
        }

        if (servico.getPreco() < 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O preço não pode ser negativo"
            );
        }

        return servicoRepository.save(servico);
    }

    public List<Servico> listar() {
        return servicoRepository.findAll()
                .stream()
                .toList();
    }

    public Servico buscarPorId(int id) {

        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Serviço não encontrado"
                ));

        return servico;
    }

    public List<Servico> listarPorTipo(String tipo) {

        Class<? extends Servico> classe = switch (tipo.toUpperCase()) {
            case "CABELO" -> Cabelo.class;
            case "PELE" -> Pele.class;
            case "SOBRANCELHA" -> Sobrancelha.class;
            case "UNHA" -> Unha.class;
            case "MASSAGEM" -> Massagem.class;
            case "MAQUIAGEM" -> Maquiagem.class;

            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo de serviço inválido"
            );
        };

        return servicoRepository.buscarPorTipo(classe)
                .stream()
                .toList();
    }
}