package br.edu.cantrace.alertas;

import br.edu.cantrace.telemetria.events.TelemetriaRecebidaEvent;

public class UmidadeBaixaRule implements AlertaRule {

    private static final double LIMITE_INFERIOR = 20.0;

    @Override
    public AlertaRegraResultado avaliar(TelemetriaRecebidaEvent event) {
        if (event.getUmidade() < LIMITE_INFERIOR) {
            return new AlertaRegraResultado(
                TipoAlerta.UMIDADE_BAIXA,
                NivelAlerta.WARN,
                "Umidade Baixa",
                String.format("Umidade atingiu %.1f%% (limite: %.1f%%)", event.getUmidade(), LIMITE_INFERIOR)
            );
        }
        return null;
    }
}
