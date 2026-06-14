package br.edu.cantrace.mqtt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import br.edu.cantrace.mqtt.events.MqttMessageProcessedEvent;
import br.edu.cantrace.mqtt.events.MqttMessageReceivedEvent;
import br.edu.cantrace.mqtt.events.MqttMessageRejectedEvent;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MqttEventPublisherTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private MqttEventPublisher mqttEventPublisher;

    @Test
    void devePublicarEventoRecebido() {
        mqttEventPublisher.publishReceived("cantrace/telemetria/ESP32-001", "payload");

        verify(eventPublisher).publishEvent(any(MqttMessageReceivedEvent.class));
    }

    @Test
    void devePublicarEventoRejeitado() {
        mqttEventPublisher.publishRejected("cantrace/telemetria/ESP32-001", "payload", "erro");

        verify(eventPublisher).publishEvent(any(MqttMessageRejectedEvent.class));
    }

    @Test
    void devePublicarEventoProcessado() {
        mqttEventPublisher.publishProcessed("cantrace/telemetria/ESP32-001", "payload", "leitura-1", "disp-1");

        verify(eventPublisher).publishEvent(any(MqttMessageProcessedEvent.class));
    }
}
