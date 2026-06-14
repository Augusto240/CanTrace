package br.edu.cantrace.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MqttMessageHandlerTest {

    @Mock
    private MqttIntegrationService integrationService;

    @Mock
    private MqttEventPublisher mqttEventPublisher;

    private MqttMessageHandler handler;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        handler = new MqttMessageHandler(integrationService, mqttEventPublisher, objectMapper);
    }

    @Test
    void deveProcessarMensagemValida() throws Exception {
        String json = "{\"codigoDispositivo\":\"ESP32-001\",\"temperatura\":25.0,\"umidade\":60.0,\"luminosidade\":800}";
        MqttMessage mqttMsg = new MqttMessage(json.getBytes());

        handler.messageArrived("cantrace/telemetria/ESP32-001", mqttMsg);

        verify(integrationService).processar(any(MQTTMessage.class));
        verify(mqttEventPublisher).publishReceived(eq("cantrace/telemetria/ESP32-001"), anyString());
    }

    @Test
    void deveRejeitarMensagemJsonInvalido() throws Exception {
        String json = "not-json";
        MqttMessage mqttMsg = new MqttMessage(json.getBytes());

        handler.messageArrived("cantrace/telemetria/invalid", mqttMsg);

        verify(integrationService, never()).processar(any());
        verify(mqttEventPublisher).publishRejected(eq("cantrace/telemetria/invalid"), anyString(), anyString());
    }

    @Test
    void deveRejeitarMensagemCamposObrigatorios() throws Exception {
        String json = "{\"temperatura\":25.0}";
        MqttMessage mqttMsg = new MqttMessage(json.getBytes());

        handler.messageArrived("cantrace/telemetria/incomplete", mqttMsg);

        verify(integrationService, never()).processar(any());
        verify(mqttEventPublisher).publishRejected(eq("cantrace/telemetria/incomplete"), anyString(), anyString());
    }

    @Test
    void deveTratarErroNoProcessamento() throws Exception {
        String json = "{\"codigoDispositivo\":\"ESP32-001\",\"temperatura\":25.0,\"umidade\":60.0,\"luminosidade\":800}";
        MqttMessage mqttMsg = new MqttMessage(json.getBytes());

        doThrow(new RuntimeException("DB error")).when(integrationService).processar(any());

        handler.messageArrived("cantrace/telemetria/ESP32-001", mqttMsg);

        verify(mqttEventPublisher).publishRejected(eq("cantrace/telemetria/ESP32-001"), anyString(), anyString());
    }
}