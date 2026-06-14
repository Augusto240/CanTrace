package br.edu.cantrace.audit.events;

import org.springframework.context.ApplicationEvent;

public class AuditEvent extends ApplicationEvent {

    private final String entidade;
    private final String entidadeId;
    private final String acao;
    private final String dadosNovos;
    private final String usuario;
    private final String ipAddress;
    private final String userAgent;

    public AuditEvent(Object source, String entidade, String entidadeId,
                      String acao, String dadosNovos, String usuario,
                      String ipAddress, String userAgent) {
        super(source);
        this.entidade = entidade;
        this.entidadeId = entidadeId;
        this.acao = acao;
        this.dadosNovos = dadosNovos;
        this.usuario = usuario;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    public String getEntidade() { return entidade; }
    public String getEntidadeId() { return entidadeId; }
    public String getAcao() { return acao; }
    public String getDadosNovos() { return dadosNovos; }
    public String getUsuario() { return usuario; }
    public String getIpAddress() { return ipAddress; }
    public String getUserAgent() { return userAgent; }
}
