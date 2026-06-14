package br.edu.cantrace.alertas.events;

import br.edu.cantrace.alertas.AlertaAmbiental;

public class AlertaCriadoEvent extends AlertaEvent {

    public AlertaCriadoEvent(Object source, AlertaAmbiental alerta) {
        super(source, alerta);
    }
}
