package br.edu.insper.projeto_final_objetos.agendamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    List<Agendamento> findByClienteIdAndConfirmadoAndAtivoTrue(
            Integer clienteId,
            boolean confirmado
    );
}