package br.edu.cantrace.alertas;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import br.edu.cantrace.security.CustomAccessDeniedHandler;
import br.edu.cantrace.security.CustomAuthenticationEntryPoint;
import br.edu.cantrace.security.SecurityConfig;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlertaController.class)
@Import({SecurityConfig.class, CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class})
class AlertaSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertaService alertaService;

    @MockBean
    private AlertaRepository alertaRepository;

    @Test
    void deveNegarAcessoSemAutenticacao() throws Exception {
        mockMvc.perform(get("/api/v1/alertas"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void devePermitirListarParaOperator() throws Exception {
        when(alertaRepository.findAll(
            org.mockito.ArgumentMatchers.any(org.springframework.data.jpa.domain.Specification.class),
            org.mockito.ArgumentMatchers.any(org.springframework.data.domain.PageRequest.class)
        )).thenReturn(new org.springframework.data.domain.PageImpl<>(java.util.List.of()));

        mockMvc.perform(get("/api/v1/alertas")
                .header("Authorization", "Basic b3BlcmF0b3I6b3BlcmF0b3IxMjM="))
            .andExpect(status().isOk());
    }

    @Test
    void deveNegarResolverParaOperator() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(put("/api/v1/alertas/" + id + "/resolver")
                .header("Authorization", "Basic b3BlcmF0b3I6b3BlcmF0b3IxMjM="))
            .andExpect(status().isForbidden());
    }

    @Test
    void devePermitirResolverParaAdmin() throws Exception {
        UUID id = UUID.randomUUID();

        when(alertaService.resolver(
            org.mockito.ArgumentMatchers.eq(id),
            org.mockito.ArgumentMatchers.anyString()
        )).thenThrow(new IllegalArgumentException("Alerta nao encontrado"));

        mockMvc.perform(put("/api/v1/alertas/" + id + "/resolver")
                .header("Authorization", "Basic YWRtaW46YWRtaW4xMjM="))
            .andExpect(status().isNotFound());
    }

    @Test
    void deveNegarIgnorarParaOperator() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(put("/api/v1/alertas/" + id + "/ignorar")
                .header("Authorization", "Basic b3BlcmF0b3I6b3BlcmF0b3IxMjM="))
            .andExpect(status().isForbidden());
    }
}
