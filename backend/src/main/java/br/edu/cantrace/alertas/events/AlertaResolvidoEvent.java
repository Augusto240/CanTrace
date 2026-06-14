package br.edu.cantrace.alertas.events;

import br.edu.cantrace.alertas.AlertaAmbiental;

public class AlertaResolvidoEvent extends AlertaEvent {

    public AlertaResolvidoEvent(Object source, AlertaAmbiental alerta) {
        super(source, alerta);
    }
}
