package br.edu.insper.projeto_final_objetos.agendamento;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CriarAgendamentoRequest(
        @NotNull Integer servicoId,
        @NotNull LocalDateTime dataHora
) {}
