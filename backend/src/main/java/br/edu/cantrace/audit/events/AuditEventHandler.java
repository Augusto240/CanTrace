package br.edu.cantrace.audit.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.edu.cantrace.audit.AuditLog;
import br.edu.cantrace.audit.AuditRepository;

@Component
public class AuditEventHandler {

    private static final Logger log = LoggerFactory.getLogger(AuditEventHandler.class);

    private final AuditRepository auditRepository;

    public AuditEventHandler(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @EventListener
    public void handleAuditEvent(AuditEvent event) {
        try {
            AuditLog auditLog = new AuditLog(
                event.getEntidade(),
                event.getEntidadeId(),
                event.getAcao(),
                null,
                event.getDadosNovos(),
                event.getUsuario(),
                event.getIpAddress(),
                event.getUserAgent()
            );
            auditRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Error persisting audit event: ", e);
        }
    }
}
