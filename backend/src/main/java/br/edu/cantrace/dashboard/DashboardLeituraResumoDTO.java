package br.edu.cantrace.dashboard;

import java.time.Instant;
import java.util.UUID;

public record DashboardLeituraResumoDTO(
    UUID dispositivoId,
    String dispositivoNome,
    String area,
    Double temperatura,
    Double umidade,
    Double luminosidade,
    Instant timestamp
) {}
