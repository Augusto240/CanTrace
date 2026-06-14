package br.edu.cantrace.mqtt;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.cantrace.dispositivos.DispositivoIoT;
import br.edu.cantrace.dispositivos.DispositivoRepository;
import br.edu.cantrace.dispositivos.DispositivoStatus;
import br.edu.cantrace.dispositivos.TipoSensor;
import br.edu.cantrace.telemetria.TelemetriaRequest;
import br.edu.cantrace.telemetria.TelemetriaService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MqttIntegrationServiceTest {

    @Mock
    private DispositivoRepository dispositivoRepository;

    @Mock
    private TelemetriaService telemetriaService;

    @Mock
    private MqttEventPublisher mqttEventPublisher;

    @InjectMocks
    private MqttIntegrationService integrationService;

    private DispositivoIoT dispositivo;

    @BeforeEach
    void setUp() {
        dispositivo = new DispositivoIoT(
            "ESP32-001",
            "Sensor Sala",
            "Sala A",
            java.util.List.of(TipoSensor.TEMPERATURA, TipoSensor.UMIDADE, TipoSensor.LUMINOSIDADE)
        );
        dispositivo.setId(UUID.randomUUID());
    }

    @Test
    void deveProcessarMensagemValida() {
        MQTTMessage msg = new MQTTMessage("ESP32-001", 25.0, 60.0, 800.0, "MQTT");

        when(dispositivoRepository.findByDeviceCode("ESP32-001")).thenReturn(Optional.of(dispositivo));
        when(telemetriaService.registrar(any(TelemetriaRequest.class))).thenReturn(null);

        integrationService.processar(msg);

        verify(telemetriaService).registrar(any(TelemetriaRequest.class));
        verify(mqttEventPublisher).publishProcessed(anyString(), anyString(), any(), anyString());
    }

    @Test
    void deveRejeitarDispositivoNaoEncontrado() {
        MQTTMessage msg = new MQTTMessage("INEXISTENTE", 25.0, 60.0, 800.0, "MQTT");

        when(dispositivoRepository.findByDeviceCode("INEXISTENTE")).thenReturn(Optional.empty());

        integrationService.processar(msg);

        verify(telemetriaService, never()).registrar(any());
        verify(mqttEventPublisher).publishRejected(anyString(), anyString(), contains("nao encontrado"));
    }

    @Test
    void deveRejeitarDispositivoInativo() {
        dispositivo.setStatus(DispositivoStatus.INATIVO);
        MQTTMessage msg = new MQTTMessage("ESP32-001", 25.0, 60.0, 800.0, "MQTT");

        when(dispositivoRepository.findByDeviceCode("ESP32-001")).thenReturn(Optional.of(dispositivo));

        integrationService.processar(msg);

        verify(telemetriaService, never()).registrar(any());
        verify(mqttEventPublisher).publishRejected(anyString(), anyString(), contains("inativo"));
    }
}
