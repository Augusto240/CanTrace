package br.edu.cantrace.mqtt.events;

public class MqttMessageReceivedEvent extends MqttEvent {
    public MqttMessageReceivedEvent(Object source, String topic, String payloadSummary) {
        super(source, topic, payloadSummary);
    }
}
