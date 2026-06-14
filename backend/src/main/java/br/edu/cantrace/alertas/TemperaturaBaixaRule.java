package br.edu.cantrace.alertas;

import br.edu.cantrace.telemetria.events.TelemetriaRecebidaEvent;

public class TemperaturaBaixaRule implements AlertaRule {

    private static final double LIMITE_INFERIOR = 10.0;

    @Override
    public AlertaRegraResultado avaliar(TelemetriaRecebidaEvent event) {
        if (event.getTemperatura() < LIMITE_INFERIOR) {
            return new AlertaRegraResultado(
                TipoAlerta.TEMPERATURA_BAIXA,
                NivelAlerta.WARN,
                "Temperatura Baixa",
                String.format("Temperatura atingiu %.1fC (limite: %.1fC)", event.getTemperatura(), LIMITE_INFERIOR)
            );
        }
        return null;
    }
}
