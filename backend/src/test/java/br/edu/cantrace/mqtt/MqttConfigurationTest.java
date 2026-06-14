package br.edu.cantrace.mqtt;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.assertj.core.api.Assertions.assertThat;

class MqttConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withPropertyValues("spring.main.allow-bean-definition-overriding=true")
        .withUserConfiguration(MockDependencies.class);

    @Test
    void naoDeveCarregarBeansQuandoMqttDesabilitado() {
        contextRunner
            .withPropertyValues("mqtt.enabled=false")
            .run(context -> {
                assertThat(context).doesNotHaveBean(MqttConfiguration.class);
            });
    }

    @Test
    void deveCarregarBeansQuandoMqttHabilitado() {
        contextRunner
            .withPropertyValues(
                "mqtt.enabled=true",
                "mqtt.broker=tcp://localhost:1883",
                "mqtt.clientId=test-client",
                "mqtt.topicTelemetria=test/topic",
                "mqtt.qos=1"
            )
            .run(context -> {
                assertThat(context).hasSingleBean(org.eclipse.paho.client.mqttv3.MqttClient.class);
            });
    }

    @Test
    void naoDeveCarregarBeansQuandoPropriedadeAusente() {
        contextRunner
            .run(context -> {
                assertThat(context).doesNotHaveBean(MqttConfiguration.class);
            });
    }

    @Configuration
    static class MockDependencies {
        @Bean
        @Primary
        public org.eclipse.paho.client.mqttv3.MqttClient mqttClient() {
            return Mockito.mock(org.eclipse.paho.client.mqttv3.MqttClient.class);
        }

        @Bean
        @Primary
        public MqttMessageHandler mqttMessageHandler() {
            return Mockito.mock(MqttMessageHandler.class);
        }
    }
}
