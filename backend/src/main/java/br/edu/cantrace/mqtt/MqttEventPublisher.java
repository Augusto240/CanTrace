package br.edu.cantrace.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import br.edu.cantrace.mqtt.events.MqttMessageProcessedEvent;
import br.edu.cantrace.mqtt.events.MqttMessageReceivedEvent;
import br.edu.cantrace.mqtt.events.MqttMessageRejectedEvent;

@Component
public class MqttEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(MqttEventPublisher.class);

    private final ApplicationEventPublisher eventPublisher;

    public MqttEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishReceived(String topic, String payloadSummary) {
        log.debug("MQTT message received from topic: {}", topic);
        eventPublisher.publishEvent(new MqttMessageReceivedEvent(this, topic, payloadSummary));
    }

    public void publishRejected(String topic, String payloadSummary, String reason) {
        log.warn("MQTT message rejected from topic: {} - reason: {}", topic, reason);
        eventPublisher.publishEvent(new MqttMessageRejectedEvent(this, topic, payloadSummary, reason));
    }

    public void publishProcessed(String topic, String payloadSummary, String leituraId, String dispositivoId) {
        log.debug("MQTT message processed from topic: {} - leitura: {}", topic, leituraId);
        eventPublisher.publishEvent(new MqttMessageProcessedEvent(this, topic, payloadSummary, leituraId, dispositivoId));
    }
}
