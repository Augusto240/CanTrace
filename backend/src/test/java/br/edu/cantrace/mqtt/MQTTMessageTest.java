package br.edu.cantrace.mqtt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MQTTMessageTest {

    @Test
    void deveCriarMensagemComTodosCampos() {
        MQTTMessage msg = new MQTTMessage("ESP32-001", 25.0, 60.0, 800.0, "MQTT");

        assertEquals("ESP32-001", msg.codigoDispositivo());
        assertEquals(25.0, msg.temperatura());
        assertEquals(60.0, msg.umidade());
        assertEquals(800.0, msg.luminosidade());
        assertEquals("MQTT", msg.origem());
    }

    @Test
    void devePermitirOrigemNula() {
        MQTTMessage msg = new MQTTMessage("ESP32-001", 25.0, 60.0, 800.0, null);

        assertNull(msg.origem());
    }

    @Test
    void deveSerIgualQuandoMesmosValores() {
        MQTTMessage msg1 = new MQTTMessage("ESP32-001", 25.0, 60.0, 800.0, "MQTT");
        MQTTMessage msg2 = new MQTTMessage("ESP32-001", 25.0, 60.0, 800.0, "MQTT");

        assertEquals(msg1, msg2);
    }
}
