package br.edu.cantrace.alertas;

import br.edu.cantrace.telemetria.events.TelemetriaRecebidaEvent;

public class LuminosidadeBaixaRule implements AlertaRule {

    private static final double LIMITE_INFERIOR = 100.0;

    @Override
    public AlertaRegraResultado avaliar(TelemetriaRecebidaEvent event) {
        if (event.getLuminosidade() < LIMITE_INFERIOR) {
            return new AlertaRegraResultado(
                TipoAlerta.LUMINOSIDADE_BAIXA,
                NivelAlerta.WARN,
                "Luminosidade Baixa",
                String.format("Luminosidade atingiu %.1f lux (limite: %.1f lux)", event.getLuminosidade(), LIMITE_INFERIOR)
            );
        }
        return null;
    }
}
