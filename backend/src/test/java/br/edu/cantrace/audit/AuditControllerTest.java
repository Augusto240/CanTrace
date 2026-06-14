package br.edu.cantrace.audit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuditController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditService auditService;

    private AuditLog auditLog;

    @BeforeEach
    void setUp() {
        auditLog = new AuditLog("Lote", "123", "CREATE_OR_UPDATE",
                null, "{\"codigo\":\"LOTE001\"}", "system", "127.0.0.1", "TestAgent");
    }

    @Test
    void testFindAll_ReturnsOk() throws Exception {
        Page<AuditLog> page = new PageImpl<>(Arrays.asList(auditLog));
        when(auditService.findAll(any(AuditFilter.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/auditoria")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].entidade").value("Lote"));
    }

    @Test
    void testFindById_ExistingId_ReturnsOk() throws Exception {
        UUID id = UUID.randomUUID();
        when(auditService.findById(id)).thenReturn(Optional.of(auditLog));

        mockMvc.perform(get("/api/v1/auditoria/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entidade").value("Lote"))
                .andExpect(jsonPath("$.entidadeId").value("123"));
    }

    @Test
    void testFindById_NonExistingId_ReturnsNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(auditService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/auditoria/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByEntidade_ReturnsOk() throws Exception {
        when(auditService.findByEntidade("Lote")).thenReturn(Arrays.asList(auditLog));

        mockMvc.perform(get("/api/v1/auditoria/entidade/{entidade}", "Lote")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].entidade").value("Lote"));
    }

    @Test
    void testGetEstatisticas_ReturnsOk() throws Exception {
        AuditStats stats = new AuditStats(10L,
                java.util.Map.of("Lote", 10L),
                java.util.Map.of("CREATE_OR_UPDATE", 7L, "DELETE", 3L));
        when(auditService.getEstatisticas()).thenReturn(stats);

        mockMvc.perform(get("/api/v1/auditoria/estatisticas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRegistros").value(10))
                .andExpect(jsonPath("$.porEntidade.Lote").value(10));
    }
}
