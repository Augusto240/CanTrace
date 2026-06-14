package br.edu.cantrace.mqtt.events;

import org.springframework.context.ApplicationEvent;

public abstract class MqttEvent extends ApplicationEvent {
    private final String topic;
    private final String payloadSummary;

    public MqttEvent(Object source, String topic, String payloadSummary) {
        super(source);
        this.topic = topic;
        this.payloadSummary = payloadSummary;
    }

    public String getTopic() { return topic; }
    public String getPayloadSummary() { return payloadSummary; }
}
