package br.edu.insper.projeto_final_objetos.servico;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarServicoRequest(

        @NotBlank
        String nome,

        @NotBlank
        String descricao,

        @NotNull
        @DecimalMin("0.0")
        Double preco,

        @NotBlank
        String tipo
) {
}