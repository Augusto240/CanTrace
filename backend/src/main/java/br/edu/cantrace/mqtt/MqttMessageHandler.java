package br.edu.cantrace.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MqttMessageHandler implements MqttCallback {

    private static final Logger log = LoggerFactory.getLogger(MqttMessageHandler.class);

    private final MqttIntegrationService integrationService;
    private final MqttEventPublisher mqttEventPublisher;
    private final ObjectMapper objectMapper;

    public MqttMessageHandler(MqttIntegrationService integrationService,
                               MqttEventPublisher mqttEventPublisher,
                               ObjectMapper objectMapper) {
        this.integrationService = integrationService;
        this.mqttEventPublisher = mqttEventPublisher;
        this.objectMapper = objectMapper;
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.warn("MQTT connection lost: {}", cause.getMessage());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Not used for subscriber
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        String payloadSummary = payload.length() > 100 ? payload.substring(0, 100) + "..." : payload;

        mqttEventPublisher.publishReceived(topic, payloadSummary);

        try {
            MQTTMessage mqttMessage = objectMapper.readValue(payload, MQTTMessage.class);

            if (mqttMessage.codigoDispositivo() == null || mqttMessage.codigoDispositivo().isBlank()) {
                throw new IllegalArgumentException("codigoDispositivo is required");
            }
            if (mqttMessage.temperatura() == null) {
                throw new IllegalArgumentException("temperatura is required");
            }
            if (mqttMessage.umidade() == null) {
                throw new IllegalArgumentException("umidade is required");
            }
            if (mqttMessage.luminosidade() == null) {
                throw new IllegalArgumentException("luminosidade is required");
            }

            integrationService.processar(mqttMessage);

        } catch (Exception e) {
            log.warn("Error processing MQTT message from topic {}: {}", topic, e.getMessage());
            mqttEventPublisher.publishRejected(topic, payloadSummary, e.getMessage());
        }
    }
}