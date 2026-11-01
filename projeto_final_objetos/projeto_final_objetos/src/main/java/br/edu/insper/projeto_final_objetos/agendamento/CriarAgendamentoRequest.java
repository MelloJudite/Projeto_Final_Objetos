package br.edu.insper.projeto_final_objetos.agendamento;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CriarAgendamentoRequest(

        @NotNull(message = "O cliente é obrigatório")
        Integer clienteId,

        @NotNull(message = "A data é obrigatória")
        @Future(message = "A data deve ser no futuro")
        LocalDateTime dataHora,

        @NotEmpty(message = "Escolha ao menos um serviço")
        List<Integer> servicoIds
) {
}