package br.edu.cantrace.dispositivos;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.edu.cantrace.dispositivos.events.DispositivoCadastradoEvent;
import br.edu.cantrace.dispositivos.events.DispositivoRemovidoEvent;
import br.edu.cantrace.dispositivos.events.DispositivoStatusAlteradoEvent;
import br.edu.cantrace.dispositivos.events.DispositivoAtualizadoEvent;
import br.edu.cantrace.telemetria.TelemetriaService;

@WebMvcTest(DispositivoController.class)
@AutoConfigureMockMvc(addFilters = false)
class DispositivoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DispositivoService dispositivoService;

    @MockBean
    private TelemetriaService telemetriaService;

    private DispositivoIoT dispositivo;
    private DispositivoRequest request;

    @BeforeEach
    void setUp() {
        dispositivo = new DispositivoIoT("cantrace-iot-01", "Sensor Sala A", "area-01",
            Arrays.asList(TipoSensor.TEMPERATURA, TipoSensor.UMIDADE));
        dispositivo.setId(UUID.randomUUID());

        request = new DispositivoRequest("cantrace-iot-01", "Sensor Sala A", "area-01",
            Arrays.asList(TipoSensor.TEMPERATURA, TipoSensor.UMIDADE));
    }

    @Test
    void criar_DeveRetornar201_ComLocation() throws Exception {
        when(dispositivoService.criar(any(DispositivoRequest.class))).thenReturn(dispositivo);

        mockMvc.perform(post("/api/v1/dispositivos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceCode\":\"cantrace-iot-01\",\"nome\":\"Sensor Sala A\",\"area\":\"area-01\",\"tipoSensors\":[\"TEMPERATURA\",\"UMIDADE\"]}"))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.deviceCode").value("cantrace-iot-01"))
            .andExpect(jsonPath("$.nome").value("Sensor Sala A"))
            .andExpect(jsonPath("$.area").value("area-01"))
            .andExpect(jsonPath("$.status").value("ATIVO"));
    }

    @Test
    void criar_DeveRetornar400_SeDeviceCodeDuplicado() throws Exception {
        when(dispositivoService.criar(any(DispositivoRequest.class)))
            .thenThrow(new IllegalArgumentException("deviceCode ja existente: cantrace-iot-01"));

        mockMvc.perform(post("/api/v1/dispositivos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceCode\":\"cantrace-iot-01\",\"nome\":\"Sensor Sala A\",\"area\":\"area-01\",\"tipoSensors\":[\"TEMPERATURA\"]}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("deviceCode ja existente: cantrace-iot-01"));
    }

    @Test
    void criar_DeveRetornar422_SeCamposObrigatoriosAusentes() throws Exception {
        mockMvc.perform(post("/api/v1/dispositivos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceCode\":\"\"}"))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void listar_DeveRetornar200_ComLista() throws Exception {
        when(dispositivoService.listar()).thenReturn(Arrays.asList(dispositivo));

        mockMvc.perform(get("/api/v1/dispositivos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].deviceCode").value("cantrace-iot-01"));
    }

    @Test
    void buscarPorId_DeveRetornar200() throws Exception {
        when(dispositivoService.buscarPorId(dispositivo.getId())).thenReturn(dispositivo);

        mockMvc.perform(get("/api/v1/dispositivos/" + dispositivo.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.deviceCode").value("cantrace-iot-01"));
    }

    @Test
    void buscarPorId_DeveRetornar404_SeNaoEncontrado() throws Exception {
        UUID id = UUID.randomUUID();
        when(dispositivoService.buscarPorId(id))
            .thenThrow(new IllegalArgumentException("Dispositivo nao encontrado: " + id));

        mockMvc.perform(get("/api/v1/dispositivos/" + id))
            .andExpect(status().isNotFound());
    }

    @Test
    void atualizar_DeveRetornar200() throws Exception {
        when(dispositivoService.atualizar(any(UUID.class), any(DispositivoRequest.class))).thenReturn(dispositivo);

        mockMvc.perform(put("/api/v1/dispositivos/" + dispositivo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"deviceCode\":\"cantrace-iot-01\",\"nome\":\"Sensor Sala A\",\"area\":\"area-01\",\"tipoSensors\":[\"TEMPERATURA\"]}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.deviceCode").value("cantrace-iot-01"));
    }

    @Test
    void alterarStatus_DeveRetornar200() throws Exception {
        dispositivo.setStatus(DispositivoStatus.MANUTENCAO);
        when(dispositivoService.alterarStatus(any(UUID.class), any(DispositivoStatus.class))).thenReturn(dispositivo);

        mockMvc.perform(patch("/api/v1/dispositivos/" + dispositivo.getId() + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"MANUTENCAO\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("MANUTENCAO"));
    }

    @Test
    void remover_DeveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/v1/dispositivos/" + dispositivo.getId()))
            .andExpect(status().isNoContent());
    }
}
