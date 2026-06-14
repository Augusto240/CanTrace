package br.edu.cantrace.audit.events;

public class AccessDeniedAuditEvent extends AuditEvent {

    public AccessDeniedAuditEvent(Object source, String usuario, String uri,
                                  String ipAddress, String userAgent) {
        super(source, "Endpoint", uri, "ACCESS_DENIED", null, usuario, ipAddress, userAgent);
    }
}
