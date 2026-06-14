package br.edu.cantrace.telemetria;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.edu.cantrace.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "leituras_ambientais")
public class LeituraAmbiental implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "dispositivo_id", nullable = false)
    private UUID dispositivoId;

    @Column(nullable = false)
    private Double temperatura;

    @Column(nullable = false)
    private Double umidade;

    @Column(nullable = false)
    private Double luminosidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrigemLeitura origem;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    public LeituraAmbiental() {
    }

    public LeituraAmbiental(UUID dispositivoId, Double temperatura, Double umidade,
                            Double luminosidade, OrigemLeitura origem, Instant timestamp) {
        this.dispositivoId = dispositivoId;
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.luminosidade = luminosidade;
        this.origem = origem;
        this.timestamp = timestamp;
        this.criadoEm = Instant.now();
    }

    @Override
    public Object getAuditableId() {
        return this.id;
    }

    @Override
    public String getAuditableEntityName() {
        return "LeituraAmbiental";
    }

    @Override
    public Map<String, Object> getAuditableData() {
        Map<String, Object> data = new HashMap<>();
        data.put("dispositivoId", this.dispositivoId);
        data.put("temperatura", this.temperatura);
        data.put("umidade", this.umidade);
        data.put("luminosidade", this.luminosidade);
        data.put("origem", this.origem != null ? this.origem.name() : null);
        data.put("timestamp", this.timestamp);
        return data;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getDispositivoId() { return dispositivoId; }
    public void setDispositivoId(UUID dispositivoId) { this.dispositivoId = dispositivoId; }
    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }
    public Double getUmidade() { return umidade; }
    public void setUmidade(Double umidade) { this.umidade = umidade; }
    public Double getLuminosidade() { return luminosidade; }
    public void setLuminosidade(Double luminosidade) { this.luminosidade = luminosidade; }
    public OrigemLeitura getOrigem() { return origem; }
    public void setOrigem(OrigemLeitura origem) { this.origem = origem; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public Instant getCriadoEm() { return criadoEm; }
    public void setCriadoEm(Instant criadoEm) { this.criadoEm = criadoEm; }
}
