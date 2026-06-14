package br.edu.cantrace.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import br.edu.cantrace.audit.AuditController;
import br.edu.cantrace.audit.AuditService;

@WebMvcTest(AuditController.class)
@Import({SecurityConfig.class, CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class})
class AuditSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditService auditService;

    @Test
    void deveRetornar401SemAutenticacao() throws Exception {
        mockMvc.perform(get("/api/v1/auditoria"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "operator", roles = {"OPERATOR"})
    void deveRetornar403ComRoleOperator() throws Exception {
        mockMvc.perform(get("/api/v1/auditoria"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void devePermitirAcessoComRoleAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/auditoria"))
            .andExpect(status().isOk());
    }
}
