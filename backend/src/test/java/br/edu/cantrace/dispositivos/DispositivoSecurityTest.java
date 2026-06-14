package br.edu.cantrace.dispositivos;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.edu.cantrace.security.CustomAccessDeniedHandler;
import br.edu.cantrace.security.CustomAuthenticationEntryPoint;
import br.edu.cantrace.security.SecurityConfig;

@WebMvcTest(DispositivoController.class)
@Import({SecurityConfig.class, CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class})
class DispositivoSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DispositivoService dispositivoService;

    @Test
    void listar_SemAutenticacao_DeveRetornar401() throws Exception {
        mockMvc.perform(get("/api/v1/dispositivos"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error").value("Unauthorized"));
    }

    @Test
    void criar_SemAutenticacao_DeveRetornar401() throws Exception {
        mockMvc.perform(post("/api/v1/dispositivos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void buscarPorId_SemAutenticacao_DeveRetornar401() throws Exception {
        mockMvc.perform(get("/api/v1/dispositivos/123e4567-e89b-12d3-a456-426614174000"))
            .andExpect(status().isUnauthorized());
    }
}
