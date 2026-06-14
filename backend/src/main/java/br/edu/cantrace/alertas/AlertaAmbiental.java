package br.edu.cantrace.alertas;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import br.edu.cantrace.audit.Auditable;

@Entity
@Table(name = "alertas_ambientais")
public class AlertaAmbiental implements Auditable {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NivelAlerta nivel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoAlerta tipo;

    @Column(name = "dispositivo_id", nullable = false, columnDefinition = "uuid")
    private UUID dispositivoId;

    @Column(name = "leitura_id", nullable = false, columnDefinition = "uuid")
    private UUID leituraId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertaStatus status;

    @Column(name = "resolvido_por", length = 100)
    private String resolvidoPor;

    @Column(name = "resolvido_em")
    private LocalDateTime resolvidoEm;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    public AlertaAmbiental(String titulo, String mensagem, NivelAlerta nivel,
                           TipoAlerta tipo, UUID dispositivoId, UUID leituraId) {
        this.id = UUID.randomUUID();
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.nivel = nivel;
        this.tipo = tipo;
        this.dispositivoId = dispositivoId;
        this.leituraId = leituraId;
        this.status = AlertaStatus.ATIVO;
        this.criadoEm = LocalDateTime.now();
    }

    protected AlertaAmbiental() {}

    public void resolver(String usuario) {
        if (this.status != AlertaStatus.ATIVO) {
            throw new IllegalStateException("Apenas alertas ATIVOS podem ser resolvidos");
        }
        this.status = AlertaStatus.RESOLVIDO;
        this.resolvidoPor = usuario;
        this.resolvidoEm = LocalDateTime.now();
    }

    public void ignorar(String usuario) {
        if (this.status != AlertaStatus.ATIVO) {
            throw new IllegalStateException("Apenas alertas ATIVOS podem ser ignorados");
        }
        this.status = AlertaStatus.IGNORADO;
        this.resolvidoPor = usuario;
        this.resolvidoEm = LocalDateTime.now();
    }

    @Override
    public String getAuditableId() {
        return id != null ? id.toString() : null;
    }

    @Override
    public String getAuditableEntityName() {
        return "AlertaAmbiental";
    }

    @Override
    public Map<String, Object> getAuditableData() {
        return Map.of(
            "titulo", titulo,
            "nivel", nivel.name(),
            "tipo", tipo.name(),
            "status", status.name()
        );
    }

    public UUID getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getMensagem() { return mensagem; }
    public NivelAlerta getNivel() { return nivel; }
    public TipoAlerta getTipo() { return tipo; }
    public UUID getDispositivoId() { return dispositivoId; }
    public UUID getLeituraId() { return leituraId; }
    public AlertaStatus getStatus() { return status; }
    public String getResolvidoPor() { return resolvidoPor; }
    public LocalDateTime getResolvidoEm() { return resolvidoEm; }
    public LocalDateTime getCriadoEm() { return criadoEm; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlertaAmbiental that = (AlertaAmbiental) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
