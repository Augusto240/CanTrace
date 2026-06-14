package br.edu.cantrace.dispositivos;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record DispositivoRequest(
    @NotBlank(message = "deviceCode e obrigatorio")
    @Size(max = 50, message = "deviceCode deve ter no maximo 50 caracteres")
    String deviceCode,

    @NotBlank(message = "nome e obrigatorio")
    @Size(max = 200, message = "nome deve ter no maximo 200 caracteres")
    String nome,

    @NotBlank(message = "area e obrigatoria")
    @Size(max = 100, message = "area deve ter no maximo 100 caracteres")
    String area,

    @NotEmpty(message = "tipoSensors deve ter pelo menos um sensor")
    List<TipoSensor> tipoSensors
) {
}
