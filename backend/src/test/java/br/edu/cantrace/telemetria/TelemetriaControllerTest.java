package br.edu.cantrace.telemetria;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TelemetriaController.class)
@AutoConfigureMockMvc(addFilters = false)
class TelemetriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TelemetriaService telemetriaService;

    private LeituraAmbiental leitura;

    @BeforeEach
    void setUp() {
        leitura = new LeituraAmbiental(UUID.randomUUID(), 25.0, 60.0, 500.0, OrigemLeitura.MANUAL, Instant.now());
        leitura.setId(UUID.randomUUID());
    }

    @Test
    void registrar_DeveRetornar201_ComLocation() throws Exception {
        when(telemetriaService.registrar(any(TelemetriaRequest.class))).thenReturn(leitura);

        mockMvc.perform(post("/api/v1/telemetria")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dispositivoId\":\"" + leitura.getDispositivoId() + "\",\"temperatura\":25.0,\"umidade\":60.0,\"luminosidade\":500.0,\"origem\":\"MANUAL\",\"timestamp\":\"2026-06-14T12:00:00Z\"}"))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.temperatura").value(25.0));
    }

    @Test
    void registrar_DeveRetornar400_SeDispositivoNaoEncontrado() throws Exception {
        when(telemetriaService.registrar(any(TelemetriaRequest.class)))
            .thenThrow(new IllegalArgumentException("Dispositivo nao encontrado"));

        mockMvc.perform(post("/api/v1/telemetria")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dispositivoId\":\"" + UUID.randomUUID() + "\",\"temperatura\":25.0,\"umidade\":60.0,\"luminosidade\":500.0,\"origem\":\"MANUAL\",\"timestamp\":\"2026-06-14T12:00:00Z\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void listar_DeveRetornar200_ComLista() throws Exception {
        when(telemetriaService.buscarHistorico(any(UUID.class), any(Integer.class), any(Integer.class)))
            .thenReturn(Arrays.asList(leitura));

        mockMvc.perform(get("/api/v1/telemetria?dispositivoId=" + leitura.getDispositivoId()))
            .andExpect(status().isOk());
    }

    @Test
    void buscarPorId_DeveRetornar200() throws Exception {
        when(telemetriaService.buscarPorId(leitura.getId())).thenReturn(Optional.of(leitura));

        mockMvc.perform(get("/api/v1/telemetria/" + leitura.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.temperatura").value(25.0));
    }

    @Test
    void buscarPorId_DeveRetornar404_SeNaoEncontrado() throws Exception {
        UUID id = UUID.randomUUID();
        when(telemetriaService.buscarPorId(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/telemetria/" + id))
            .andExpect(status().isNotFound());
    }
}
