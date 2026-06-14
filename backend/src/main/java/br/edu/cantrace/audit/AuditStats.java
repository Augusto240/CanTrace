package br.edu.cantrace.audit;

import java.util.Map;

public record AuditStats(
    long totalRegistros,
    Map<String, Long> porEntidade,
    Map<String, Long> porAcao
) {}
