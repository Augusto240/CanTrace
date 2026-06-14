package br.edu.cantrace.dispositivos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DispositivoResponse(
    UUID id,
    String deviceCode,
    String nome,
    String area,
    List<TipoSensor> tipoSensors,
    DispositivoStatus status,
    LocalDateTime dataInstalacao,
    LocalDateTime ultimaComunicacao,
    BigDecimal latitude,
    BigDecimal longitude,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {
    public static DispositivoResponse fromEntity(DispositivoIoT entity) {
        return new DispositivoResponse(
            entity.getId(),
            entity.getDeviceCode(),
            entity.getNome(),
            entity.getArea(),
            entity.getTipoSensors(),
            entity.getStatus(),
            entity.getDataInstalacao(),
            entity.getUltimaComunicacao(),
            entity.getLatitude(),
            entity.getLongitude(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm()
        );
    }
}
