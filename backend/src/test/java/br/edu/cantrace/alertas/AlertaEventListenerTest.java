package br.edu.cantrace.alertas;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.cantrace.telemetria.events.TelemetriaRecebidaEvent;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertaEventListenerTest {

    @Mock
    private AlertaRuleEngine ruleEngine;

    @Mock
    private AlertaService alertaService;

    @InjectMocks
    private AlertaEventListener alertaEventListener;

    @Test
    void deveSalvarAlertasQuandoRegraDetecta() {
        TelemetriaRecebidaEvent event = new TelemetriaRecebidaEvent(
            this, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
            35.0, 60.0, 800.0, br.edu.cantrace.telemetria.OrigemLeitura.MANUAL
        );

        AlertaRegraResultado resultado = new AlertaRegraResultado(
            TipoAlerta.TEMPERATURA_ALTA, NivelAlerta.WARN,
            "Temperatura Alta", "Temperatura atingiu 35.0C"
        );

        when(ruleEngine.avaliar(event)).thenReturn(java.util.List.of(resultado));
        when(alertaService.salvar(any(AlertaAmbiental.class))).thenAnswer(invocation -> invocation.getArgument(0));

        alertaEventListener.handleTelemetriaRecebida(event);

        verify(alertaService, times(1)).salvar(any(AlertaAmbiental.class));
    }

    @Test
    void naoDeveSalvarAlertasQuandoNenhumRegraDetecta() {
        TelemetriaRecebidaEvent event = new TelemetriaRecebidaEvent(
            this, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
            22.0, 50.0, 800.0, br.edu.cantrace.telemetria.OrigemLeitura.MANUAL
        );

        when(ruleEngine.avaliar(event)).thenReturn(java.util.List.of());

        alertaEventListener.handleTelemetriaRecebida(event);

        verify(alertaService, never()).salvar(any(AlertaAmbiental.class));
    }
}
