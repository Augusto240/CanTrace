package br.edu.cantrace.audit.events;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import br.edu.cantrace.audit.AuditLog;
import br.edu.cantrace.audit.AuditRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuditEventHandlerTest {

    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private AuditEventHandler handler;

    @Test
    void devePersistirLoginAuditEvent() {
        LoginAuditEvent event = new LoginAuditEvent(this, "admin", "192.168.1.1");

        handler.handleAuditEvent(event);

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditRepository).save(captor.capture());

        AuditLog log = captor.getValue();
        assertEquals("Usuario", log.getEntidade());
        assertEquals("admin", log.getEntidadeId());
        assertEquals("LOGIN", log.getAcao());
        assertEquals("admin", log.getUsuario());
        assertEquals("192.168.1.1", log.getIpAddress());
    }

    @Test
    void devePersistirAccessDeniedAuditEvent() {
        AccessDeniedAuditEvent event = new AccessDeniedAuditEvent(
            this, "operator", "/api/v1/auditoria", "10.0.0.1", "Mozilla/5.0"
        );

        handler.handleAuditEvent(event);

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditRepository).save(captor.capture());

        AuditLog log = captor.getValue();
        assertEquals("Endpoint", log.getEntidade());
        assertEquals("/api/v1/auditoria", log.getEntidadeId());
        assertEquals("ACCESS_DENIED", log.getAcao());
        assertEquals("operator", log.getUsuario());
        assertEquals("10.0.0.1", log.getIpAddress());
        assertEquals("Mozilla/5.0", log.getUserAgent());
    }
}
