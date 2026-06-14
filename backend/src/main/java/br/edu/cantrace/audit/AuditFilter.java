package br.edu.cantrace.audit;

import java.time.LocalDateTime;

public record AuditFilter(
    String entidade,
    String acao,
    String usuario,
    LocalDateTime dataInicio,
    LocalDateTime dataFim,
    Integer page,
    Integer size
) {
    public AuditFilter {
        if (page == null || page < 0) page = 0;
        if (size == null || size < 1) size = 20;
        if (size > 100) size = 100;
    }
}
