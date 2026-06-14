package br.edu.cantrace.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "mqtt.enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(MQTTConnectionProperties.class)
public class MqttConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MqttConfiguration.class);

    @Bean
    public MqttClient mqttClient(MQTTConnectionProperties properties,
                                  MqttMessageHandler messageHandler) throws Exception {
        log.info("Configuring MQTT client - broker: {}, clientId: {}", properties.broker(), properties.clientId());

        MqttClient client = new MqttClient(properties.broker(), properties.clientId(), new MemoryPersistence());
        client.setCallback(messageHandler);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(30);

        if (properties.username() != null && !properties.username().isBlank()) {
            options.setUserName(properties.username());
        }
        if (properties.password() != null && !properties.password().isBlank()) {
            options.setPassword(properties.password().toCharArray());
        }

        client.connect(options);
        client.subscribe(properties.topicTelemetria(), properties.qos());

        log.info("MQTT client connected and subscribed to: {}", properties.topicTelemetria());

        return client;
    }
}
