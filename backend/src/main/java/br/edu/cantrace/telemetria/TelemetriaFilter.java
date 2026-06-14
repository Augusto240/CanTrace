package br.edu.cantrace.telemetria;

import java.time.Instant;
import java.util.UUID;

public record TelemetriaFilter(
    UUID dispositivoId,
    Instant inicio,
    Instant fim,
    int page,
    int size
) {
    public TelemetriaFilter {
        if (page < 0) page = 0;
        if (size < 1) size = 20;
        if (size > 100) size = 100;
    }
}
