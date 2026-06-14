package br.edu.cantrace.audit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuditServiceTest {

    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private AuditService auditService;

    @Test
    void deveBuscarPorIdExistente() {
        UUID id = UUID.randomUUID();
        AuditLog log = new AuditLog("Lote", "123", "CREATE", null, "{}", "admin", "1.1.1.1", "UA");
        when(auditRepository.findById(id)).thenReturn(Optional.of(log));

        Optional<AuditLog> result = auditService.findById(id);

        assertTrue(result.isPresent());
        assertEquals("Lote", result.get().getEntidade());
    }

    @Test
    void deveRetornarVazioParaIdInexistente() {
        UUID id = UUID.randomUUID();
        when(auditRepository.findById(id)).thenReturn(Optional.empty());

        Optional<AuditLog> result = auditService.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    void deveListarComPaginacao() {
        AuditLog log = new AuditLog("Lote", "123", "CREATE", null, "{}", "admin", "1.1.1.1", "UA");
        Page<AuditLog> page = new PageImpl<>(List.of(log));
        when(auditRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        AuditFilter filter = new AuditFilter(null, null, null, null, null, 0, 20);
        Page<AuditLog> result = auditService.findAll(filter);

        assertEquals(1, result.getContent().size());
        assertEquals("Lote", result.getContent().get(0).getEntidade());
    }

    @Test
    void deveListarPorEntidade() {
        AuditLog log = new AuditLog("Lote", "123", "CREATE", null, "{}", "admin", "1.1.1.1", "UA");
        when(auditRepository.findByEntidade("Lote")).thenReturn(List.of(log));

        List<AuditLog> result = auditService.findByEntidade("Lote");

        assertEquals(1, result.size());
        assertEquals("Lote", result.get(0).getEntidade());
    }

    @Test
    void deveRetornarEstatisticas() {
        when(auditRepository.count()).thenReturn(10L);
        when(auditRepository.countByEntidade()).thenReturn(Map.of("Lote", 7L, "Documento", 3L));
        when(auditRepository.countByAcao()).thenReturn(Map.of("CREATE", 5L, "UPDATE", 3L, "DELETE", 2L));

        AuditStats stats = auditService.getEstatisticas();

        assertEquals(10L, stats.totalRegistros());
        assertEquals(7L, stats.porEntidade().get("Lote"));
        assertEquals(5L, stats.porAcao().get("CREATE"));
    }
}
