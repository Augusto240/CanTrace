package br.edu.cantrace.lotes;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(LoteController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoteService loteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Lote createLote() {
        Lote lote = new Lote(
            "LOT-001",
            "Produto Teste",
            LocalDate.now(),
            LocalDate.now().plusDays(30),
            new BigDecimal("100.00"),
            "Responsavel Teste",
            "Notas teste"
        );
        lote.setId(UUID.randomUUID());
        return lote;
    }

    @Test
    void deveCriarLote() throws Exception {
        Lote lote = createLote();
        when(loteService.criar(org.mockito.ArgumentMatchers.any(LoteRequest.class))).thenReturn(lote);

        mockMvc.perform(post("/api/v1/lotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoteRequest(
                    "LOT-001", "Produto Teste", LocalDate.now(),
                    LocalDate.now().plusDays(30), new BigDecimal("100.00"),
                    "Responsavel Teste", "Notas teste"
                ))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.codigo").value("LOT-001"));
    }

    @Test
    void deveListarLotes() throws Exception {
        when(loteService.listar()).thenReturn(List.of(createLote()));

        mockMvc.perform(get("/api/v1/lotes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].codigo").value("LOT-001"));
    }

    @Test
    void deveBuscarLotePorId() throws Exception {
        Lote lote = createLote();
        when(loteService.buscarPorId(lote.getId())).thenReturn(lote);

        mockMvc.perform(get("/api/v1/lotes/" + lote.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.codigo").value("LOT-001"));
    }

    @Test
    void deveRetornar404ParaLoteInexistente() throws Exception {
        UUID id = UUID.randomUUID();
        when(loteService.buscarPorId(id)).thenThrow(new IllegalArgumentException("Lote nao encontrado"));

        mockMvc.perform(get("/api/v1/lotes/" + id))
            .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarLote() throws Exception {
        Lote lote = createLote();
        when(loteService.atualizar(eq(lote.getId()), org.mockito.ArgumentMatchers.any(LoteRequest.class))).thenReturn(lote);

        mockMvc.perform(put("/api/v1/lotes/" + lote.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoteRequest(
                    "LOT-001", "Produto Teste", LocalDate.now(),
                    LocalDate.now().plusDays(30), new BigDecimal("100.00"),
                    "Responsavel Teste", "Notas teste"
                ))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.codigo").value("LOT-001"));
    }

    @Test
    void deveRemoverLote() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/lotes/" + id))
            .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar422ParaDadosInvalidos() throws Exception {
        mockMvc.perform(post("/api/v1/lotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isUnprocessableEntity());
    }
}
