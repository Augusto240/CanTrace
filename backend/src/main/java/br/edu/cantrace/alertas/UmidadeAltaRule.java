package br.edu.cantrace.alertas;

import br.edu.cantrace.telemetria.events.TelemetriaRecebidaEvent;

public class UmidadeAltaRule implements AlertaRule {

    private static final double LIMITE_SUPERIOR = 90.0;

    @Override
    public AlertaRegraResultado avaliar(TelemetriaRecebidaEvent event) {
        if (event.getUmidade() > LIMITE_SUPERIOR) {
            return new AlertaRegraResultado(
                TipoAlerta.UMIDADE_ALTA,
                NivelAlerta.WARN,
                "Umidade Alta",
                String.format("Umidade atingiu %.1f%% (limite: %.1f%%)", event.getUmidade(), LIMITE_SUPERIOR)
            );
        }
        return null;
    }
}
