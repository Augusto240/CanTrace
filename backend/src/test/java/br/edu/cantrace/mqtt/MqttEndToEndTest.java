package br.edu.cantrace.mqtt;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationEventPublisher;

import br.edu.cantrace.dispositivos.DispositivoIoT;
import br.edu.cantrace.dispositivos.DispositivoRepository;
import br.edu.cantrace.dispositivos.DispositivoStatus;
import br.edu.cantrace.dispositivos.TipoSensor;
import br.edu.cantrace.telemetria.TelemetriaService;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MqttEndToEndTest {

    @Mock
    private DispositivoRepository dispositivoRepository;

    @Mock
    private TelemetriaService telemetriaService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private MqttEventPublisher mqttEventPublisher;
    private MqttIntegrationService integrationService;
    private MqttMessageHandler handler;

    @BeforeEach
    void setUp() {
        mqttEventPublisher = new MqttEventPublisher(eventPublisher);
        integrationService = new MqttIntegrationService(dispositivoRepository, telemetriaService, mqttEventPublisher);
        handler = new MqttMessageHandler(integrationService, mqttEventPublisher, new ObjectMapper());

        DispositivoIoT dispositivo = new DispositivoIoT(
            "ESP32-001", "Sensor Sala", "Sala A",
            java.util.List.of(TipoSensor.TEMPERATURA, TipoSensor.UMIDADE, TipoSensor.LUMINOSIDADE)
        );
        dispositivo.setId(UUID.randomUUID());
        when(dispositivoRepository.findByDeviceCode("ESP32-001")).thenReturn(Optional.of(dispositivo));
        when(telemetriaService.registrar(any())).thenReturn(null);
    }

    @Test
    void fluxoCompletoMensagemValida() throws Exception {
        String json = "{\"codigoDispositivo\":\"ESP32-001\",\"temperatura\":25.0,\"umidade\":60.0,\"luminosidade\":800}";
        org.eclipse.paho.client.mqttv3.MqttMessage mqttMsg =
            new org.eclipse.paho.client.mqttv3.MqttMessage(json.getBytes());

        handler.messageArrived("cantrace/telemetria/ESP32-001", mqttMsg);

        verify(eventPublisher, atLeastOnce()).publishEvent(any());
        verify(telemetriaService).registrar(any());
    }

    @Test
    void fluxoCompletoMensagemInvalida() throws Exception {
        String json = "invalid-json";
        org.eclipse.paho.client.mqttv3.MqttMessage mqttMsg =
            new org.eclipse.paho.client.mqttv3.MqttMessage(json.getBytes());

        handler.messageArrived("cantrace/telemetria/invalid", mqttMsg);

        verify(telemetriaService, never()).registrar(any());
    }

    @Test
    void fluxoCompletoDispositivoNaoEncontrado() throws Exception {
        when(dispositivoRepository.findByDeviceCode("UNKNOWN")).thenReturn(Optional.empty());

        String json = "{\"codigoDispositivo\":\"UNKNOWN\",\"temperatura\":25.0,\"umidade\":60.0,\"luminosidade\":800}";
        org.eclipse.paho.client.mqttv3.MqttMessage mqttMsg =
            new org.eclipse.paho.client.mqttv3.MqttMessage(json.getBytes());

        handler.messageArrived("cantrace/telemetria/UNKNOWN", mqttMsg);

        verify(telemetriaService, never()).registrar(any());
    }
}
