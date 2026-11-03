package br.edu.insper.projeto_final_objetos.agendamento;

import br.edu.insper.projeto_final_objetos.auth.Perfil;
import br.edu.insper.projeto_final_objetos.auth.UsuarioAutenticado;
import br.edu.insper.projeto_final_objetos.cliente.ClienteService;
import br.edu.insper.projeto_final_objetos.profissional.ProfissionalRepository;
import br.edu.insper.projeto_final_objetos.servico.Servico;
import br.edu.insper.projeto_final_objetos.servico.ServicoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AgendamentoService {
    private final AgendamentoRepository repository;
    private final ClienteService clienteService;
    private final ProfissionalRepository profissionalRepository;
    private final ServicoService servicoService;

    public AgendamentoService(AgendamentoRepository repository, ClienteService clienteService,
            ProfissionalRepository profissionalRepository, ServicoService servicoService) {
        this.repository = repository;
        this.clienteService = clienteService;
        this.profissionalRepository = profissionalRepository;
        this.servicoService = servicoService;
    }

    @Transactional(readOnly = true)
    public List<String> horariosDisponiveis(Integer servicoId, LocalDate data) {
        servicoService.buscarPorId(servicoId);
        if (data == null || data.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selecione uma data válida");
        }
        LocalDateTime inicio = data.atTime(8, 0);
        LocalDateTime fim = data.atTime(18, 0);
        return java.util.stream.IntStream.range(8, 18)
                .mapToObj(h -> LocalTime.of(h, 0))
                .filter(h -> !LocalDateTime.of(data, h).isBefore(LocalDateTime.now()))
                .map(LocalTime::toString).toList();
    }

    @Transactional
    public Agendamento reservar(CriarAgendamentoRequest request, UsuarioAutenticado usuario) {
        exigirPerfil(usuario, Perfil.CLIENTE);
        var cliente = clienteService.buscarPorId(usuario.getId());
        Servico servico = servicoService.buscarPorId(request.servicoId());
        LocalDateTime dataHora = request.dataHora();
        if (dataHora == null || dataHora.isBefore(LocalDateTime.now()) ||
                dataHora.getMinute() != 0 || dataHora.getSecond() != 0 ||
                dataHora.getNano() != 0 || dataHora.getHour() < 8 || dataHora.getHour() >= 18) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Escolha um horário válido entre 08h e 17h, em intervalos de uma hora");
        }
        Agendamento a = new Agendamento();
        a.setCliente(cliente);
        a.setDataHora(dataHora);
        a.setStatus(StatusAgendamento.PENDENTE);
        a.getServicos().add(servico);
        return repository.save(a);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarCliente(Integer clienteId) {
        return repository.findByClienteIdAndAtivoTrueOrderByDataHoraDesc(clienteId)
                .stream().map(AgendamentoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarSolicitacoes(Integer profissionalId) {
        return repository.findByStatusAndAtivoTrueOrderByDataHoraAsc(StatusAgendamento.PENDENTE)
                .stream().map(AgendamentoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarAgenda(Integer profissionalId) {
        return repository.findByProfissionalIdAndStatusAndAtivoTrueOrderByDataHoraAsc(
                profissionalId, StatusAgendamento.ACEITO)
                .stream().map(AgendamentoResponse::from).toList();
    }

    @Transactional
    public AgendamentoResponse aceitar(Integer agendamentoId, Integer profissionalId) {
        Agendamento a = repository.buscarParaAtualizacao(agendamentoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação não encontrada"));
        if (!a.isAtivo() || a.getStatus() != StatusAgendamento.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta solicitação já foi atendida por outro profissional ou cancelada");
        }
        var profissional = profissionalRepository.buscarParaAtualizacao(profissionalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional não encontrado"));
        if (repository.existsByProfissionalIdAndDataHoraAndStatusAndAtivoTrue(
                profissionalId, a.getDataHora(), StatusAgendamento.ACEITO)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Você já possui um atendimento neste horário");
        }
        a.setProfissional(profissional);
        a.setStatus(StatusAgendamento.ACEITO);
        try {
            return AgendamentoResponse.from(repository.saveAndFlush(a));
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O horário acabou de ser ocupado");
        }
    }

    @Transactional
    public void cancelarCliente(Integer id, Integer clienteId) {
        Agendamento a = repository.buscarParaAtualizacao(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));
        if (!a.getCliente().getId().equals(clienteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode cancelar esta solicitação");
        }
        if (a.getDataHora().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível cancelar um horário que já passou");
        }
        a.setAtivo(false);
        a.setStatus(StatusAgendamento.CANCELADO);
        repository.save(a);
    }

    @Transactional
    public void cancelarProfissional(Integer id, Integer profissionalId) {
        Agendamento a = repository.buscarParaAtualizacao(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));
        if (a.getProfissional() == null || !a.getProfissional().getId().equals(profissionalId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Este atendimento não pertence a você");
        }
        if (a.getDataHora().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível cancelar um atendimento passado");
        }
        a.setAtivo(false);
        a.setStatus(StatusAgendamento.CANCELADO);
        repository.save(a);
    }

    private void exigirPerfil(UsuarioAutenticado usuario, Perfil perfil) {
        if (usuario == null || usuario.getPerfil() != perfil) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Perfil sem permissão para esta operação");
        }
    }
}
