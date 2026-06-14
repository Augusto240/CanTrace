package br.edu.cantrace.telemetria;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import br.edu.cantrace.security.CustomAccessDeniedHandler;
import br.edu.cantrace.security.CustomAuthenticationEntryPoint;
import br.edu.cantrace.security.SecurityConfig;

@WebMvcTest(TelemetriaController.class)
@Import({SecurityConfig.class, CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class})
class TelemetriaSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TelemetriaService telemetriaService;

    @Test
    void listar_SemAutenticacao_DeveRetornar401() throws Exception {
        mockMvc.perform(get("/api/v1/telemetria?dispositivoId=" + UUID.randomUUID()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "operator", roles = {"OPERATOR"})
    void listar_ComOperator_DevePermitir() throws Exception {
        mockMvc.perform(get("/api/v1/telemetria?dispositivoId=" + UUID.randomUUID()))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "operator", roles = {"OPERATOR"})
    void registrar_ComOperator_DeveNegar() throws Exception {
        mockMvc.perform(post("/api/v1/telemetria")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void registrar_ComAdmin_DevePermitir() throws Exception {
        mockMvc.perform(post("/api/v1/telemetria")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isUnprocessableEntity());
    }
}
