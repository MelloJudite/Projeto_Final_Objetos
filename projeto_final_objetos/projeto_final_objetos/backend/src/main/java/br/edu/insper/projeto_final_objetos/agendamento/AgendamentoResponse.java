package br.edu.insper.projeto_final_objetos.agendamento;

import java.time.LocalDateTime;
import java.util.List;

public record AgendamentoResponse(
        Integer id,
        Integer clienteId,
        String clienteNome,
        Integer profissionalId,
        String profissionalNome,
        LocalDateTime dataHora,
        String status,
        Integer servicoId,
        String servicoNome,
        String descricao,
        double valor
) {
    public static AgendamentoResponse from(Agendamento a) {
        var s = a.getServicos().isEmpty() ? null : a.getServicos().get(0);
        return new AgendamentoResponse(
                a.getId(), a.getCliente().getId(), a.getCliente().getNome(),
                a.getProfissional() == null ? null : a.getProfissional().getId(),
                a.getProfissional() == null ? null : a.getProfissional().getNome(),
                a.getDataHora(), a.getStatus() == null ? "PENDENTE" : a.getStatus().name(),
                s == null ? null : s.getId(), s == null ? "Serviço" : s.getNome(),
                s == null ? "" : s.getDescricao(), s == null ? 0 : s.getPreco()
        );
    }
}
