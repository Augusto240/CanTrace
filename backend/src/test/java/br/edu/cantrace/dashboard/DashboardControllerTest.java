package br.edu.cantrace.dashboard;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import br.edu.cantrace.security.CustomAccessDeniedHandler;
import br.edu.cantrace.security.CustomAuthenticationEntryPoint;
import br.edu.cantrace.security.SecurityConfig;

@WebMvcTest(DashboardController.class)
@Import({SecurityConfig.class, CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class})
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    private DashboardDTO dashboardDTO;

    @BeforeEach
    void setUp() {
        DashboardLeituraResumoDTO leitura = new DashboardLeituraResumoDTO(
            UUID.randomUUID(),
            "Sensor Sala A",
            "Sala A",
            25.0,
            60.0,
            450.0,
            Instant.now()
        );

        dashboardDTO = new DashboardDTO(
            12, 3, 2, 1,
            150L, 850L,
            24.5, 65.2, 450.0,
            8L, 15L, 3L,
            250L,
            List.of(leitura)
        );
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornar200QuandoAdminAcessaDashboard() throws Exception {
        when(dashboardService.buscarDashboard()).thenReturn(dashboardDTO);

        mockMvc.perform(get("/api/v1/dashboard"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalDispositivosAtivo").value(12))
            .andExpect(jsonPath("$.totalDispositivosInativo").value(3))
            .andExpect(jsonPath("$.totalLeituras24h").value(150))
            .andExpect(jsonPath("$.totalAlertasAtivo").value(8))
            .andExpect(jsonPath("$.totalRegistrosAuditoria").value(250))
            .andExpect(jsonPath("$.ultimasLeituras").isArray())
            .andExpect(jsonPath("$.ultimasLeituras[0].area").value("Sala A"));
    }

    @Test
    @WithMockUser(username = "operator", roles = {"OPERATOR"})
    void deveRetornar200QuandoOperatorAcessaDashboard() throws Exception {
        when(dashboardService.buscarDashboard()).thenReturn(dashboardDTO);

        mockMvc.perform(get("/api/v1/dashboard"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalDispositivosAtivo").value(12));
    }

    @Test
    void deveRetornar401QuandoNaoAutenticado() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard"))
            .andExpect(status().isUnauthorized());
    }
}
