package br.edu.cantrace.telemetria;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TelemetriaRequest(
    @NotNull(message = "dispositivoId e obrigatorio")
    UUID dispositivoId,

    @NotNull(message = "temperatura e obrigatoria")
    @Min(value = -50, message = "temperatura minima e -50")
    @Max(value = 100, message = "temperatura maxima e 100")
    Double temperatura,

    @NotNull(message = "umidade e obrigatoria")
    @Min(value = 0, message = "umidade minima e 0")
    @Max(value = 100, message = "umidade maxima e 100")
    Double umidade,

    @NotNull(message = "luminosidade e obrigatoria")
    @Min(value = 0, message = "luminosidade minima e 0")
    Double luminosidade,

    @NotNull(message = "origem e obrigatoria")
    OrigemLeitura origem,

    @NotNull(message = "timestamp e obrigatorio")
    Instant timestamp
) {
}
