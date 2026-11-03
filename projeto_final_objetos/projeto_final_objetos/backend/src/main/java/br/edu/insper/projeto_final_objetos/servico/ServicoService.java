
package br.edu.insper.projeto_final_objetos.servico;

import br.edu.insper.projeto_final_objetos.profissional.ProfissionalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final ProfissionalRepository profissionalRepository;

    public ServicoService(
            ServicoRepository servicoRepository,
            ProfissionalRepository profissionalRepository
    ) {
        this.servicoRepository = servicoRepository;
        this.profissionalRepository = profissionalRepository;
    }

    public Servico salvar(Servico servico, Integer profissionalId) {

        var profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Profissional não encontrado"
                ));

        if (servico.getNome() == null || servico.getNome().isBlank()
                || servico.getDescricao() == null || servico.getDescricao().isBlank()
                || !Double.isFinite(servico.getPreco()) || servico.getPreco() <= 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Confira os dados do serviço"
            );
        }

        servico.setProfissional(profissional);

        return servicoRepository.save(servico);
    }

    public List<Servico> listar() {
        return servicoRepository.findAll();
    }

    public Servico buscarPorId(int id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Serviço não encontrado"
                ));
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

        return servicoRepository.buscarPorTipo(classe);
    }
}
