package br.edu.cantrace.alertas;

import br.edu.cantrace.telemetria.events.TelemetriaRecebidaEvent;

public class TemperaturaAltaRule implements AlertaRule {

    private static final double LIMITE_SUPERIOR = 30.0;

    @Override
    public AlertaRegraResultado avaliar(TelemetriaRecebidaEvent event) {
        if (event.getTemperatura() > LIMITE_SUPERIOR) {
            return new AlertaRegraResultado(
                TipoAlerta.TEMPERATURA_ALTA,
                NivelAlerta.WARN,
                "Temperatura Alta",
                String.format("Temperatura atingiu %.1fC (limite: %.1fC)", event.getTemperatura(), LIMITE_SUPERIOR)
            );
        }
        return null;
    }
}
