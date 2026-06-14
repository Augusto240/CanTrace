package br.edu.cantrace.alertas;

import br.edu.cantrace.telemetria.events.TelemetriaRecebidaEvent;

public interface AlertaRule {
    AlertaRegraResultado avaliar(TelemetriaRecebidaEvent event);
}
