package br.edu.cantrace.dispositivos.events;

import java.util.List;

import br.edu.cantrace.dispositivos.TipoSensor;

public class DispositivoCadastradoEvent extends DispositivoEvent {
    private final String area;
    private final List<TipoSensor> tipoSensors;

    public DispositivoCadastradoEvent(Object source, String dispositivoId, String deviceCode,
                                       String area, List<TipoSensor> tipoSensors) {
        super(source, dispositivoId, deviceCode);
        this.area = area;
        this.tipoSensors = tipoSensors;
    }

    public String getArea() { return area; }
    public List<TipoSensor> getTipoSensors() { return tipoSensors; }
}
