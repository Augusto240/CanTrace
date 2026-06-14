package br.edu.cantrace.dispositivos.events;

public class DispositivoRemovidoEvent extends DispositivoEvent {
    public DispositivoRemovidoEvent(Object source, String dispositivoId, String deviceCode) {
        super(source, dispositivoId, deviceCode);
    }
}
