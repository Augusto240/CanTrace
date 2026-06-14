package br.edu.cantrace.mqtt.events;

public class MqttMessageProcessedEvent extends MqttEvent {
    private final String leituraId;
    private final String dispositivoId;

    public MqttMessageProcessedEvent(Object source, String topic, String payloadSummary,
                                      String leituraId, String dispositivoId) {
        super(source, topic, payloadSummary);
        this.leituraId = leituraId;
        this.dispositivoId = dispositivoId;
    }

    public String getLeituraId() { return leituraId; }
    public String getDispositivoId() { return dispositivoId; }
}
