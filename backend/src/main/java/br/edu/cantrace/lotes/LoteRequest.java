package br.edu.cantrace.lotes;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record LoteRequest(
    @NotBlank(message = "Codigo e obrigatorio")
    @Size(max = 50, message = "Codigo deve ter no maximo 50 caracteres")
    String codigo,

    @NotBlank(message = "Nome do produto e obrigatorio")
    @Size(max = 200, message = "Nome do produto deve ter no maximo 200 caracteres")
    String nomeProduto,

    @NotNull(message = "Data de entrada e obrigatoria")
    LocalDate dataEntrada,

    @NotNull(message = "Data de validade e obrigatoria")
    LocalDate dataValidade,

    @NotNull(message = "Quantidade inicial e obrigatoria")
    @Positive(message = "Quantidade inicial deve ser positiva")
    BigDecimal quantidadeInicial,

    @NotBlank(message = "Responsavel e obrigatorio")
    @Size(max = 200, message = "Responsavel deve ter no maximo 200 caracteres")
    String responsavel,

    String notas
) {
}
