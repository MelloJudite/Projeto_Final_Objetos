package br.edu.insper.projeto_final_objetos.agendamento;

import br.edu.insper.projeto_final_objetos.cliente.Cliente;
import br.edu.insper.projeto_final_objetos.cliente.ClienteService;
import br.edu.insper.projeto_final_objetos.servico.Servico;
import br.edu.insper.projeto_final_objetos.servico.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ServicoService servicoService;

    @Transactional
    public Agendamento adicionarAoCarrinho(CriarAgendamentoRequest request) {

        Cliente cliente = clienteService.buscarPorId(request.clienteId());

        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setDataHora(request.dataHora());
        agendamento = agendamentoRepository.save(agendamento);

        for (Integer idServico : request.servicoIds().stream().distinct().toList()) {
            Servico servico = servicoService.buscarPorId(idServico);

            servico.getAgendamentos().add(agendamento);
            agendamento.getServicos().add(servico);
        }

        return agendamento;
    }

    public List<Agendamento> listar(Integer clienteId, boolean confirmado) {
        return agendamentoRepository
                .findByClienteIdAndConfirmadoAndAtivoTrue(clienteId, confirmado);
    }

    public Agendamento confirmar(int id) {

        Agendamento agendamento = buscarPorId(id);

        if (agendamento.isConfirmado()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O agendamento já está confirmado"
            );
        }

        if (agendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data do agendamento já passou"
            );
        }

        agendamento.setConfirmado(true);

        return agendamentoRepository.save(agendamento);
    }

    // exclusão lógica
    public void cancelar(int id) {

        Agendamento agendamento = buscarPorId(id);

        agendamento.setAtivo(false);

        agendamentoRepository.save(agendamento);
    }

    public Agendamento buscarPorId(int id) {

        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Agendamento não encontrado"
                ));

        if (!agendamento.isAtivo()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Agendamento não encontrado"
            );
        }

        return agendamento;
    }
}