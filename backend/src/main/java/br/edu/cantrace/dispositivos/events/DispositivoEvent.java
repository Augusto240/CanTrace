package br.edu.cantrace.dispositivos.events;

import org.springframework.context.ApplicationEvent;

public abstract class DispositivoEvent extends ApplicationEvent {
    private final String dispositivoId;
    private final String deviceCode;

    protected DispositivoEvent(Object source, String dispositivoId, String deviceCode) {
        super(source);
        this.dispositivoId = dispositivoId;
        this.deviceCode = deviceCode;
    }

    public String getDispositivoId() { return dispositivoId; }
    public String getDeviceCode() { return deviceCode; }
}
