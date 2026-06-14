package br.edu.cantrace.alertas;

import br.edu.cantrace.telemetria.events.TelemetriaRecebidaEvent;

public class LuminosidadeAltaRule implements AlertaRule {

    private static final double LIMITE_SUPERIOR = 2000.0;

    @Override
    public AlertaRegraResultado avaliar(TelemetriaRecebidaEvent event) {
        if (event.getLuminosidade() > LIMITE_SUPERIOR) {
            return new AlertaRegraResultado(
                TipoAlerta.LUMINOSIDADE_ALTA,
                NivelAlerta.WARN,
                "Luminosidade Alta",
                String.format("Luminosidade atingiu %.1f lux (limite: %.1f lux)", event.getLuminosidade(), LIMITE_SUPERIOR)
            );
        }
        return null;
    }
}
