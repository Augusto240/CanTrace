package br.edu.cantrace.alertas;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.cantrace.telemetria.events.TelemetriaRecebidaEvent;

import static org.junit.jupiter.api.Assertions.*;

class AlertaRuleEngineTest {

    private AlertaRuleEngine ruleEngine;

    @BeforeEach
    void setUp() {
        ruleEngine = new AlertaRuleEngine();
    }

    @Test
    void deveAvaliarTemperaturaAlta() {
        TelemetriaRecebidaEvent event = new TelemetriaRecebidaEvent(
            this, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
            35.0, 60.0, 800.0, br.edu.cantrace.telemetria.OrigemLeitura.MANUAL
        );

        List<AlertaRegraResultado> resultados = ruleEngine.avaliar(event);

        assertTrue(resultados.stream().anyMatch(r -> r.tipo() == TipoAlerta.TEMPERATURA_ALTA));
    }

    @Test
    void deveAvaliarTemperaturaBaixa() {
        TelemetriaRecebidaEvent event = new TelemetriaRecebidaEvent(
            this, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
            5.0, 60.0, 800.0, br.edu.cantrace.telemetria.OrigemLeitura.MANUAL
        );

        List<AlertaRegraResultado> resultados = ruleEngine.avaliar(event);

        assertTrue(resultados.stream().anyMatch(r -> r.tipo() == TipoAlerta.TEMPERATURA_BAIXA));
    }

    @Test
    void deveAvaliarUmidadeAlta() {
        TelemetriaRecebidaEvent event = new TelemetriaRecebidaEvent(
            this, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
            22.0, 95.0, 800.0, br.edu.cantrace.telemetria.OrigemLeitura.MANUAL
        );

        List<AlertaRegraResultado> resultados = ruleEngine.avaliar(event);

        assertTrue(resultados.stream().anyMatch(r -> r.tipo() == TipoAlerta.UMIDADE_ALTA));
    }

    @Test
    void deveAvaliarUmidadeBaixa() {
        TelemetriaRecebidaEvent event = new TelemetriaRecebidaEvent(
            this, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
            22.0, 15.0, 800.0, br.edu.cantrace.telemetria.OrigemLeitura.MANUAL
        );

        List<AlertaRegraResultado> resultados = ruleEngine.avaliar(event);

        assertTrue(resultados.stream().anyMatch(r -> r.tipo() == TipoAlerta.UMIDADE_BAIXA));
    }

    @Test
    void deveAvaliarLuminosidadeAlta() {
        TelemetriaRecebidaEvent event = new TelemetriaRecebidaEvent(
            this, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
            22.0, 50.0, 2500.0, br.edu.cantrace.telemetria.OrigemLeitura.MANUAL
        );

        List<AlertaRegraResultado> resultados = ruleEngine.avaliar(event);

        assertTrue(resultados.stream().anyMatch(r -> r.tipo() == TipoAlerta.LUMINOSIDADE_ALTA));
    }

    @Test
    void deveAvaliarLuminosidadeBaixa() {
        TelemetriaRecebidaEvent event = new TelemetriaRecebidaEvent(
            this, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
            22.0, 50.0, 50.0, br.edu.cantrace.telemetria.OrigemLeitura.MANUAL
        );

        List<AlertaRegraResultado> resultados = ruleEngine.avaliar(event);

        assertTrue(resultados.stream().anyMatch(r -> r.tipo() == TipoAlerta.LUMINOSIDADE_BAIXA));
    }

    @Test
    void deveRetornarListaVaziaQuandoNenhumAlerta() {
        TelemetriaRecebidaEvent event = new TelemetriaRecebidaEvent(
            this, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
            22.0, 50.0, 800.0, br.edu.cantrace.telemetria.OrigemLeitura.MANUAL
        );

        List<AlertaRegraResultado> resultados = ruleEngine.avaliar(event);

        assertTrue(resultados.isEmpty());
    }

    @Test
    void deveRetornarMultiplosAlertas() {
        TelemetriaRecebidaEvent event = new TelemetriaRecebidaEvent(
            this, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
            35.0, 95.0, 2500.0, br.edu.cantrace.telemetria.OrigemLeitura.MANUAL
        );

        List<AlertaRegraResultado> resultados = ruleEngine.avaliar(event);

        assertEquals(3, resultados.size());
    }
}
