package br.edu.cantrace.mqtt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mqtt")
public record MQTTConnectionProperties(
    boolean enabled,
    String broker,
    String clientId,
    String username,
    String password,
    String topicTelemetria,
    int qos,
    long reconnectDelayMs,
    int maxReconnectAttempts
) {}
