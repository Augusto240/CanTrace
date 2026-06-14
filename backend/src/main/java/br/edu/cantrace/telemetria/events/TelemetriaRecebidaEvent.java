package br.edu.cantrace.telemetria.events;

import br.edu.cantrace.telemetria.OrigemLeitura;

public class TelemetriaRecebidaEvent extends TelemetriaEvent {
    private final Double temperatura;
    private final Double umidade;
    private final Double luminosidade;
    private final OrigemLeitura origem;

    public TelemetriaRecebidaEvent(Object source, String leituraId, String dispositivoId,
                                   Double temperatura, Double umidade, Double luminosidade,
                                   OrigemLeitura origem) {
        super(source, leituraId, dispositivoId);
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.luminosidade = luminosidade;
        this.origem = origem;
    }

    public Double getTemperatura() { return temperatura; }
    public Double getUmidade() { return umidade; }
    public Double getLuminosidade() { return luminosidade; }
    public OrigemLeitura getOrigem() { return origem; }
}
