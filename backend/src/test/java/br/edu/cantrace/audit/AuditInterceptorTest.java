package br.edu.cantrace.audit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.cantrace.lotes.Lote;
import br.edu.cantrace.lotes.LoteStatus;
import br.edu.cantrace.shared.RequestContextService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuditInterceptorTest {

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private RequestContextService requestContextService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuditInterceptor interceptor;

    @Test
    void deveRegistrarAuditoriaComContexto() throws Exception {
        Lote lote = new Lote();
        lote.setId(UUID.randomUUID());
        lote.setCodigo("LOT-001");
        lote.setStatus(LoteStatus.ATIVO);

        when(requestContextService.getUsername()).thenReturn("admin");
        when(requestContextService.getClientIp()).thenReturn("192.168.1.100");
        when(requestContextService.getUserAgent()).thenReturn("Mozilla/5.0");
        when(requestContextService.getUri()).thenReturn("/api/v1/lotes");
        when(requestContextService.getMethod()).thenReturn("POST");
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"codigo\":\"LOT-001\"}");

        interceptor.interceptSave(null, lote);

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditRepository).save(captor.capture());

        AuditLog log = captor.getValue();
        assertEquals("Lote", log.getEntidade());
        assertEquals("admin", log.getUsuario());
        assertEquals("192.168.1.100", log.getIpAddress());
        assertEquals("Mozilla/5.0", log.getUserAgent());
        assertEquals("/api/v1/lotes", log.getUri());
        assertEquals("POST", log.getMetodoHttp());
    }

    @Test
    void deveIgnorarAuditableLog() {
        AuditLog auditLog = new AuditLog();
        interceptor.interceptSave(null, auditLog);
        verify(auditRepository, never()).save(any());
    }

    @Test
    void deveUsarAuditableInterface() throws Exception {
        Lote lote = new Lote();
        lote.setId(UUID.randomUUID());
        lote.setCodigo("LOT-001");

        when(requestContextService.getUsername()).thenReturn("system");
        when(requestContextService.getClientIp()).thenReturn("unknown");
        when(requestContextService.getUserAgent()).thenReturn("unknown");
        when(requestContextService.getUri()).thenReturn("/api/v1/lotes");
        when(requestContextService.getMethod()).thenReturn("PUT");
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"codigo\":\"LOT-001\"}");

        interceptor.interceptSave(null, lote);

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditRepository).save(captor.capture());

        AuditLog log = captor.getValue();
        assertNotNull(log.getDadosNovos());
        assertTrue(log.getDadosNovos().contains("LOT-001"));
    }

    @Test
    void deveInterceptarDelete() throws Exception {
        Lote lote = new Lote();
        lote.setId(UUID.randomUUID());
        lote.setCodigo("LOT-001");

        JoinPoint joinPoint = mock(JoinPoint.class);
        when(joinPoint.getArgs()).thenReturn(new Object[]{lote});

        when(requestContextService.getUsername()).thenReturn("admin");
        when(requestContextService.getClientIp()).thenReturn("192.168.1.100");
        when(requestContextService.getUserAgent()).thenReturn("Mozilla/5.0");
        when(requestContextService.getUri()).thenReturn("/api/v1/lotes/123");
        when(requestContextService.getMethod()).thenReturn("DELETE");
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"codigo\":\"LOT-001\"}");

        interceptor.interceptDelete(joinPoint);

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditRepository).save(captor.capture());

        AuditLog log = captor.getValue();
        assertEquals("DELETE", log.getAcao());
        assertNotNull(log.getDadosAnteriores());
    }
}
