package br.edu.cantrace.telemetria.events;

import org.springframework.context.ApplicationEvent;

public abstract class TelemetriaEvent extends ApplicationEvent {
    private final String leituraId;
    private final String dispositivoId;

    protected TelemetriaEvent(Object source, String leituraId, String dispositivoId) {
        super(source);
        this.leituraId = leituraId;
        this.dispositivoId = dispositivoId;
    }

    public String getLeituraId() { return leituraId; }
    public String getDispositivoId() { return dispositivoId; }
}
