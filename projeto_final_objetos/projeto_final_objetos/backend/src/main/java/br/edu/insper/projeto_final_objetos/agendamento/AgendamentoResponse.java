package br.edu.insper.projeto_final_objetos.agendamento;

import br.edu.insper.projeto_final_objetos.servico.Servico;

import java.time.LocalDateTime;
import java.util.List;

public record AgendamentoResponse(
        Integer id,
        Integer clienteId,
        LocalDateTime dataHora,
        boolean confirmado,
        List<Servico> servicos,
        double valorTotal
) {

    public static AgendamentoResponse from(Agendamento agendamento) {
        return new AgendamentoResponse(
                agendamento.getId(),
                agendamento.getCliente().getId(),
                agendamento.getDataHora(),
                agendamento.isConfirmado(),
                agendamento.getServicos(),
                agendamento.getServicos().stream()
                        .mapToDouble(Servico::getPreco)
                        .sum()
        );
    }
}