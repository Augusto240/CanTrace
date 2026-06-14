package br.edu.cantrace.audit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class AuditRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
        .withDatabaseName("cantrace_test")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private AuditRepository auditRepository;

    @Test
    void devePersistirEBuscarAuditLog() {
        AuditLog log = new AuditLog("Lote", "123", "CREATE", null,
            "{\"codigo\":\"LOT-001\"}", "admin", "192.168.1.1", "Mozilla/5.0");
        log.setUri("/api/v1/lotes");
        log.setMetodoHttp("POST");

        AuditLog saved = auditRepository.save(log);

        assertNotNull(saved.getId());
        assertEquals("Lote", saved.getEntidade());
        assertEquals("admin", saved.getUsuario());

        var found = auditRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Lote", found.get().getEntidade());
    }

    @Test
    void deveBuscarPorEntidade() {
        auditRepository.save(new AuditLog("Lote", "1", "CREATE", null, "{}", "admin", "1.1.1.1", "UA"));
        auditRepository.save(new AuditLog("Lote", "2", "UPDATE", null, "{}", "admin", "1.1.1.1", "UA"));
        auditRepository.save(new AuditLog("Documento", "1", "CREATE", null, "{}", "admin", "1.1.1.1", "UA"));

        List<AuditLog> lotes = auditRepository.findByEntidade("Lote");

        assertEquals(2, lotes.size());
    }

    @Test
    void deveContarPorEntidade() {
        auditRepository.save(new AuditLog("Lote", "1", "CREATE", null, "{}", "admin", "1.1.1.1", "UA"));
        auditRepository.save(new AuditLog("Lote", "2", "CREATE", null, "{}", "admin", "1.1.1.1", "UA"));
        auditRepository.save(new AuditLog("Documento", "1", "CREATE", null, "{}", "admin", "1.1.1.1", "UA"));

        Map<String, Long> count = auditRepository.countByEntidadeRaw().stream()
            .collect(java.util.stream.Collectors.toMap(
                row -> (String) row[0],
                row -> (Long) row[1]));

        assertEquals(2L, count.get("Lote"));
        assertEquals(1L, count.get("Documento"));
    }
}
