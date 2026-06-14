package br.edu.cantrace.dispositivos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
@Table(name = "dispositivos")
public class DispositivoIoT implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "device_code", nullable = false, unique = true, length = 50)
    private String deviceCode;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(nullable = false, length = 100)
    private String area;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tipo_sensors", nullable = false, columnDefinition = "jsonb")
    private List<TipoSensor> tipoSensors = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DispositivoStatus status;

    @Column(name = "data_instalacao", nullable = false)
    private LocalDateTime dataInstalacao;

    @Column(name = "ultima_comunicacao")
    private LocalDateTime ultimaComunicacao;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public DispositivoIoT() {
    }

    public DispositivoIoT(String deviceCode, String nome, String area, List<TipoSensor> tipoSensors) {
        this.deviceCode = deviceCode;
        this.nome = nome;
        this.area = area;
        this.tipoSensors = tipoSensors != null ? new ArrayList<>(tipoSensors) : new ArrayList<>();
        this.status = DispositivoStatus.ATIVO;
        this.dataInstalacao = LocalDateTime.now();
        this.criadoEm = LocalDateTime.now();
    }

    public void atualizarDados(String nome, String area, List<TipoSensor> tipoSensors) {
        this.nome = nome;
        this.area = area;
        this.tipoSensors = tipoSensors != null ? new ArrayList<>(tipoSensors) : this.tipoSensors;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void alterarStatus(DispositivoStatus novoStatus) {
        this.status = novoStatus;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void softDelete() {
        this.status = DispositivoStatus.INATIVO;
        this.atualizadoEm = LocalDateTime.now();
    }

    @Override
    public Object getAuditableId() {
        return this.id;
    }

    @Override
    public String getAuditableEntityName() {
        return "DispositivoIoT";
    }

    @Override
    public Map<String, Object> getAuditableData() {
        Map<String, Object> data = new HashMap<>();
        data.put("deviceCode", this.deviceCode);
        data.put("nome", this.nome);
        data.put("area", this.area);
        data.put("status", this.status != null ? this.status.name() : null);
        data.put("tipoSensors", this.tipoSensors);
        return data;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getDeviceCode() { return deviceCode; }
    public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public List<TipoSensor> getTipoSensors() { return tipoSensors; }
    public void setTipoSensors(List<TipoSensor> tipoSensors) { this.tipoSensors = tipoSensors; }
    public DispositivoStatus getStatus() { return status; }
    public void setStatus(DispositivoStatus status) { this.status = status; }
    public LocalDateTime getDataInstalacao() { return dataInstalacao; }
    public void setDataInstalacao(LocalDateTime dataInstalacao) { this.dataInstalacao = dataInstalacao; }
    public LocalDateTime getUltimaComunicacao() { return ultimaComunicacao; }
    public void setUltimaComunicacao(LocalDateTime ultimaComunicacao) { this.ultimaComunicacao = ultimaComunicacao; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
