package br.edu.cantrace.alertas;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.edu.cantrace.telemetria.events.TelemetriaRecebidaEvent;

@Component
public class AlertaEventListener {

    private final AlertaRuleEngine ruleEngine;
    private final AlertaService alertaService;

    public AlertaEventListener(AlertaRuleEngine ruleEngine, AlertaService alertaService) {
        this.ruleEngine = ruleEngine;
        this.alertaService = alertaService;
    }

    @EventListener
    public void handleTelemetriaRecebida(TelemetriaRecebidaEvent event) {
        List<AlertaRegraResultado> resultados = ruleEngine.avaliar(event);

        for (AlertaRegraResultado resultado : resultados) {
            AlertaAmbiental alerta = new AlertaAmbiental(
                resultado.titulo(),
                resultado.mensagem(),
                resultado.nivel(),
                resultado.tipo(),
                UUID.fromString(event.getDispositivoId()),
                UUID.fromString(event.getLeituraId())
            );
            alertaService.salvar(alerta);
        }
    }
}
