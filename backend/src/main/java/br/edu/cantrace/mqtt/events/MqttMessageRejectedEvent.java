package br.edu.cantrace.mqtt.events;

public class MqttMessageRejectedEvent extends MqttEvent {
    private final String reason;

    public MqttMessageRejectedEvent(Object source, String topic, String payloadSummary, String reason) {
        super(source, topic, payloadSummary);
        this.reason = reason;
    }

    public String getReason() { return reason; }
}
