package br.edu.cantrace.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import br.edu.cantrace.lotes.LoteController;
import br.edu.cantrace.lotes.LoteService;

@WebMvcTest(LoteController.class)
@Import({SecurityConfig.class, CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class})
class LoteControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoteService loteService;

    @Test
    void deveRetornar401SemAutenticacao() throws Exception {
        mockMvc.perform(get("/api/v1/lotes"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error").value("Unauthorized"));
    }

    @Test
    void deveRetornar401ComAutenticacaoInvalida() throws Exception {
        mockMvc.perform(get("/api/v1/lotes")
                .header("Authorization", "Basic d3Jvbmc6d3Jvbmc="))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void devePermitirGetComAutenticacao() throws Exception {
        mockMvc.perform(get("/api/v1/lotes"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void devePermitirPostComAutenticacao() throws Exception {
        mockMvc.perform(post("/api/v1/lotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornar401SemAutenticacaoParaPost() throws Exception {
        mockMvc.perform(post("/api/v1/lotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isUnauthorized());
    }
}
