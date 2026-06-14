package br.edu.cantrace.alertas;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlertaController.class)
@AutoConfigureMockMvc(addFilters = false)
class AlertaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertaService alertaService;

    @MockBean
    private AlertaRepository alertaRepository;

    @Test
    void deveListarAlertas() throws Exception {
        when(alertaRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(
            new org.springframework.data.domain.PageImpl<>(java.util.List.of())
        );

        mockMvc.perform(get("/api/v1/alertas"))
            .andExpect(status().isOk());
    }

    @Test
    void deveBuscarAlertaPorId() throws Exception {
        UUID id = UUID.randomUUID();
        AlertaAmbiental alerta = new AlertaAmbiental(
            "Teste", "mensagem", NivelAlerta.WARN, TipoAlerta.TEMPERATURA_ALTA,
            UUID.randomUUID(), UUID.randomUUID()
        );

        when(alertaRepository.findById(id)).thenReturn(Optional.of(alerta));

        mockMvc.perform(get("/api/v1/alertas/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.titulo").value("Teste"));
    }

    @Test
    void deveRetornar404QuandoAlertaNaoEncontrado() throws Exception {
        UUID id = UUID.randomUUID();
        when(alertaRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/alertas/" + id))
            .andExpect(status().isNotFound());
    }

    @Test
    void deveResolverAlerta() throws Exception {
        UUID id = UUID.randomUUID();
        AlertaAmbiental alerta = new AlertaAmbiental(
            "Teste", "mensagem", NivelAlerta.WARN, TipoAlerta.TEMPERATURA_ALTA,
            UUID.randomUUID(), UUID.randomUUID()
        );
        alerta.resolver("admin");

        when(alertaService.resolver(eq(id), eq("admin"))).thenReturn(alerta);

        mockMvc.perform(put("/api/v1/alertas/" + id + "/resolver")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"usuario\":\"admin\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("RESOLVIDO"));
    }

    @Test
    void deveIgnorarAlerta() throws Exception {
        UUID id = UUID.randomUUID();
        AlertaAmbiental alerta = new AlertaAmbiental(
            "Teste", "mensagem", NivelAlerta.INFO, TipoAlerta.LUMINOSIDADE_ALTA,
            UUID.randomUUID(), UUID.randomUUID()
        );
        alerta.ignorar("operator");

        when(alertaService.ignorar(eq(id), eq("operator"))).thenReturn(alerta);

        mockMvc.perform(put("/api/v1/alertas/" + id + "/ignorar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"usuario\":\"operator\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("IGNORADO"));
    }
}
