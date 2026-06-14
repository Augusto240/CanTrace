package br.edu.cantrace.dispositivos.events;

import java.util.List;

import br.edu.cantrace.dispositivos.TipoSensor;

public class DispositivoAtualizadoEvent extends DispositivoEvent {
    private final String nome;
    private final String area;
    private final List<TipoSensor> tipoSensors;

    public DispositivoAtualizadoEvent(Object source, String dispositivoId, String deviceCode,
                                       String nome, String area, List<TipoSensor> tipoSensors) {
        super(source, dispositivoId, deviceCode);
        this.nome = nome;
        this.area = area;
        this.tipoSensors = tipoSensors;
    }

    public String getNome() { return nome; }
    public String getArea() { return area; }
    public List<TipoSensor> getTipoSensors() { return tipoSensors; }
}
