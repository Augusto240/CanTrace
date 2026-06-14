package br.edu.cantrace.mqtt;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.edu.cantrace.dispositivos.DispositivoIoT;
import br.edu.cantrace.dispositivos.DispositivoRepository;
import br.edu.cantrace.dispositivos.DispositivoStatus;
import br.edu.cantrace.telemetria.OrigemLeitura;
import br.edu.cantrace.telemetria.TelemetriaRequest;
import br.edu.cantrace.telemetria.TelemetriaService;

@Service
public class MqttIntegrationService {

    private static final Logger log = LoggerFactory.getLogger(MqttIntegrationService.class);

    private final DispositivoRepository dispositivoRepository;
    private final TelemetriaService telemetriaService;
    private final MqttEventPublisher mqttEventPublisher;

    public MqttIntegrationService(DispositivoRepository dispositivoRepository,
                                   TelemetriaService telemetriaService,
                                   MqttEventPublisher mqttEventPublisher) {
        this.dispositivoRepository = dispositivoRepository;
        this.telemetriaService = telemetriaService;
        this.mqttEventPublisher = mqttEventPublisher;
    }

    public void processar(MQTTMessage message) {
        String topic = "cantrace/telemetria/" + message.codigoDispositivo();
        String payloadSummary = String.format("{temp:%s,umid:%s,lum:%s}",
            message.temperatura(), message.umidade(), message.luminosidade());

        try {
            DispositivoIoT dispositivo = dispositivoRepository.findByDeviceCode(message.codigoDispositivo())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Dispositivo nao encontrado: " + message.codigoDispositivo()));

            if (dispositivo.getStatus() == DispositivoStatus.INATIVO) {
                throw new IllegalArgumentException(
                    "Dispositivo esta inativo: " + message.codigoDispositivo());
            }

            TelemetriaRequest request = new TelemetriaRequest(
                dispositivo.getId(),
                message.temperatura(),
                message.umidade(),
                message.luminosidade(),
                OrigemLeitura.MQTT,
                Instant.now()
            );

            telemetriaService.registrar(request);

            mqttEventPublisher.publishProcessed(topic, payloadSummary,
                "pending", dispositivo.getId().toString());

            log.info("MQTT telemetry processed for device: {}", message.codigoDispositivo());

        } catch (IllegalArgumentException e) {
            mqttEventPublisher.publishRejected(topic, payloadSummary, e.getMessage());
            log.warn("MQTT message rejected: {}", e.getMessage());
        } catch (Exception e) {
            mqttEventPublisher.publishRejected(topic, payloadSummary, "Processing error: " + e.getMessage());
            log.error("Error processing MQTT message", e);
        }
    }
}
