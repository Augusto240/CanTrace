package br.edu.cantrace.audit.events;

public class LoginAuditEvent extends AuditEvent {

    public LoginAuditEvent(Object source, String username, String ipAddress) {
        super(source, "Usuario", username, "LOGIN", null, username, ipAddress, null);
    }
}
