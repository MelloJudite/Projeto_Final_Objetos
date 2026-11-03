package br.edu.insper.projeto_final_objetos.agendamento;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
    List<Agendamento> findByClienteIdAndAtivoTrueOrderByDataHoraDesc(Integer clienteId);
    List<Agendamento> findByStatusAndAtivoTrueOrderByDataHoraAsc(StatusAgendamento status);
    List<Agendamento> findByProfissionalIdAndStatusAndAtivoTrueOrderByDataHoraAsc(
            Integer profissionalId, StatusAgendamento status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Agendamento a where a.id = :id")
    Optional<Agendamento> buscarParaAtualizacao(@Param("id") Integer id);

    List<Agendamento> findByDataHoraBetweenAndStatusAndAtivoTrue(
            LocalDateTime inicio, LocalDateTime fim, StatusAgendamento status);

    boolean existsByProfissionalIdAndDataHoraAndStatusAndAtivoTrue(
            Integer profissionalId, LocalDateTime dataHora, StatusAgendamento status);
}
