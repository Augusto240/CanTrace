package br.edu.cantrace.alertas.events;

import org.springframework.context.ApplicationEvent;

import br.edu.cantrace.alertas.AlertaAmbiental;

public class AlertaEvent extends ApplicationEvent {

    private final AlertaAmbiental alerta;

    public AlertaEvent(Object source, AlertaAmbiental alerta) {
        super(source);
        this.alerta = alerta;
    }

    public AlertaAmbiental getAlerta() {
        return alerta;
    }
}
