package br.edu.cantrace.alertas;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.edu.cantrace.telemetria.events.TelemetriaRecebidaEvent;

@Component
public class AlertaRuleEngine {

    private final List<AlertaRule> rules;

    public AlertaRuleEngine() {
        this.rules = new ArrayList<>();
        this.rules.add(new TemperaturaAltaRule());
        this.rules.add(new TemperaturaBaixaRule());
        this.rules.add(new UmidadeAltaRule());
        this.rules.add(new UmidadeBaixaRule());
        this.rules.add(new LuminosidadeAltaRule());
        this.rules.add(new LuminosidadeBaixaRule());
    }

    public List<AlertaRegraResultado> avaliar(TelemetriaRecebidaEvent event) {
        List<AlertaRegraResultado> resultados = new ArrayList<>();
        for (AlertaRule rule : rules) {
            AlertaRegraResultado resultado = rule.avaliar(event);
            if (resultado != null) {
                resultados.add(resultado);
            }
        }
        return resultados;
    }
}
