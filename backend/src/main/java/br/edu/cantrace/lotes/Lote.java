package br.edu.cantrace.lotes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import br.edu.cantrace.audit.Auditable;

@Entity
@Table(name = "lotes")
public class Lote implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "nome_produto", nullable = false, length = 200)
    private String nomeProduto;

    @Column(name = "data_entrada", nullable = false)
    private LocalDate dataEntrada;

    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoteStatus status;

    @Column(name = "quantidade_inicial", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeInicial;

    @Column(name = "quantidade_atual", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeAtual;

    @Column(nullable = false, length = 200)
    private String responsavel;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public Lote() {
    }

    public Lote(String codigo, String nomeProduto, LocalDate dataEntrada, LocalDate dataValidade,
                BigDecimal quantidadeInicial, String responsavel, String notas) {
        this.codigo = codigo;
        this.nomeProduto = nomeProduto;
        this.dataEntrada = dataEntrada;
        this.dataValidade = dataValidade;
        this.status = LoteStatus.RASCUNHO;
        this.quantidadeInicial = quantidadeInicial;
        this.quantidadeAtual = quantidadeInicial;
        this.responsavel = responsavel;
        this.notas = notas;
        this.criadoEm = LocalDateTime.now();
    }

    public void softDelete() {
        this.status = LoteStatus.DESCARTADO;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizarStatus(LoteStatus novoStatus) {
        this.status = novoStatus;
        this.atualizadoEm = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public LoteStatus getStatus() {
        return status;
    }

    public void setStatus(LoteStatus status) {
        this.status = status;
    }

    public BigDecimal getQuantidadeInicial() {
        return quantidadeInicial;
    }

    public void setQuantidadeInicial(BigDecimal quantidadeInicial) {
        this.quantidadeInicial = quantidadeInicial;
    }

    public BigDecimal getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(BigDecimal quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    @Override
    public Object getAuditableId() {
        return this.id;
    }

    @Override
    public String getAuditableEntityName() {
        return "Lote";
    }

    @Override
    public Map<String, Object> getAuditableData() {
        Map<String, Object> data = new HashMap<>();
        data.put("codigo", this.codigo);
        data.put("nomeProduto", this.nomeProduto);
        data.put("status", this.status != null ? this.status.name() : null);
        data.put("quantidadeAtual", this.quantidadeAtual);
        data.put("responsavel", this.responsavel);
        return data;
    }
}
