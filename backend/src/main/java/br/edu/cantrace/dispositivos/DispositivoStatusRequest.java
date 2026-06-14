package br.edu.cantrace.dispositivos;

import jakarta.validation.constraints.NotNull;

public record DispositivoStatusRequest(
    @NotNull(message = "status e obrigatorio")
    DispositivoStatus status
) {
}
