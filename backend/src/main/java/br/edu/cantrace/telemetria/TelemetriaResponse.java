package br.edu.cantrace.telemetria;

import java.time.Instant;
import java.util.UUID;

public record TelemetriaResponse(
    UUID id,
    UUID dispositivoId,
    Double temperatura,
    Double umidade,
    Double luminosidade,
    OrigemLeitura origem,
    Instant timestamp,
    Instant criadoEm
) {
    public static TelemetriaResponse fromEntity(LeituraAmbiental entity) {
        return new TelemetriaResponse(
            entity.getId(),
            entity.getDispositivoId(),
            entity.getTemperatura(),
            entity.getUmidade(),
            entity.getLuminosidade(),
            entity.getOrigem(),
            entity.getTimestamp(),
            entity.getCriadoEm()
        );
    }
}
