package br.edu.cantrace.lotes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record LoteResponse(
    UUID id,
    String codigo,
    String nomeProduto,
    LocalDate dataEntrada,
    LocalDate dataValidade,
    LoteStatus status,
    BigDecimal quantidadeInicial,
    BigDecimal quantidadeAtual,
    String responsavel,
    String notas,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {
    public static LoteResponse fromEntity(Lote lote) {
        return new LoteResponse(
            lote.getId(),
            lote.getCodigo(),
            lote.getNomeProduto(),
            lote.getDataEntrada(),
            lote.getDataValidade(),
            lote.getStatus(),
            lote.getQuantidadeInicial(),
            lote.getQuantidadeAtual(),
            lote.getResponsavel(),
            lote.getNotas(),
            lote.getCriadoEm(),
            lote.getAtualizadoEm()
        );
    }
}
