/*
  CanTrace IoT Station - Firmware base
  - Wi-Fi e MQTT
  - Telemetria periodica (temperatura, umidade, luz)
  - Comandos para LED e buzzer
  - Fallback com valores simulados
*/

#include <Arduino.h>

#if defined(ESP32)
#include <WiFi.h>
#elif defined(ESP8266)
#include <ESP8266WiFi.h>
#else
#error "Plataforma nao suportada. Use ESP32 ou ESP8266."
#endif

#include <PubSubClient.h>
#include <DHT.h>

// Wi-Fi
const char* WIFI_SSID = "SEU_SSID";
const char* WIFI_PASSWORD = "SUA_SENHA";

// MQTT
const char* MQTT_HOST = "192.168.0.10";
const uint16_t MQTT_PORT = 1883;

// Identificacao
const char* DEVICE_ID = "cantrace-iot-01";

// Topicos MQTT
const char* TOPIC_TELEMETRY_TEMP = "cantrace/area-01/telemetry/temperature";
const char* TOPIC_TELEMETRY_HUM = "cantrace/area-01/telemetry/humidity";
const char* TOPIC_TELEMETRY_LIGHT = "cantrace/area-01/telemetry/light";
const char* TOPIC_STATUS = "cantrace/area-01/status";
const char* TOPIC_ALERT = "cantrace/area-01/alert";
const char* TOPIC_CMD_LED = "cantrace/area-01/command/led";
const char* TOPIC_CMD_BUZZER = "cantrace/area-01/command/buzzer";

// Pinos (ajuste conforme a placa)
#if defined(ESP32)
const int LED_PIN = 2;     // LED interno em muitas placas
const int BUZZER_PIN = 15; // Ajuste conforme o buzzer
const int DHT_PIN = 4;
const int LDR_PIN = 34;    // ADC1
const bool LED_ACTIVE_LOW = false;
#else
const int LED_PIN = LED_BUILTIN; // LED interno (ativo em nivel baixo)
const int BUZZER_PIN = 5;        // D1 (GPIO 5)
const int DHT_PIN = 4;           // D2 (GPIO 4)
const int LDR_PIN = A0;          // ADC
const bool LED_ACTIVE_LOW = true;
#endif

#define DHT_TYPE DHT22 // Altere para DHT11 se necessario

// Ative ou desative sensores reais
bool dhtEnabled = true;
bool ldrEnabled = true;

DHT dht(DHT_PIN, DHT_TYPE);
WiFiClient wifiClient;
PubSubClient client(wifiClient);

const unsigned long PUBLISH_INTERVAL_MS = 10000;
const unsigned long WIFI_RECONNECT_TIMEOUT_MS = 15000;
unsigned long lastPublish = 0;

void setLed(bool on) {
  if (LED_ACTIVE_LOW) {
    digitalWrite(LED_PIN, on ? LOW : HIGH);
  } else {
    digitalWrite(LED_PIN, on ? HIGH : LOW);
  }
}

void setBuzzer(bool on) {
  digitalWrite(BUZZER_PIN, on ? HIGH : LOW);
}

void setupWiFi() {
  Serial.print("Conectando ao Wi-Fi: ");
  Serial.println(WIFI_SSID);
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println();
  Serial.print("Wi-Fi conectado. IP: ");
  Serial.println(WiFi.localIP());
}

void ensureWiFi() {
  if (WiFi.status() == WL_CONNECTED) {
    return;
  }

  Serial.println("Wi-Fi desconectado. Tentando reconectar...");
  WiFi.disconnect();
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  unsigned long start = millis();
  while (WiFi.status() != WL_CONNECTED && millis() - start < WIFI_RECONNECT_TIMEOUT_MS) {
    delay(500);
    Serial.print(".");
  }

  Serial.println();
  if (WiFi.status() == WL_CONNECTED) {
    Serial.print("Wi-Fi reconectado. IP: ");
    Serial.println(WiFi.localIP());
  } else {
    Serial.println("Falha ao reconectar Wi-Fi.");
  }
}

void connectMqtt() {
  if (WiFi.status() != WL_CONNECTED) {
    return;
  }

  while (!client.connected()) {
    String clientId = String(DEVICE_ID) + "-" + String(random(0xffff), HEX);
    Serial.print("Conectando ao MQTT...");

    if (client.connect(clientId.c_str())) {
      Serial.println(" conectado.");
      client.subscribe(TOPIC_CMD_LED, 1);
      client.subscribe(TOPIC_CMD_BUZZER, 1);
    } else {
      Serial.print(" falhou, rc=");
      Serial.print(client.state());
      Serial.println(". Tentando novamente em 2s.");
      delay(2000);
    }
  }
}

bool parseOnOff(String msg, bool* outState) {
  msg.trim();
  msg.toUpperCase();

  if (msg.indexOf("\"STATE\":\"ON\"") >= 0) {
    *outState = true;
    return true;
  }
  if (msg.indexOf("\"STATE\":\"OFF\"") >= 0) {
    *outState = false;
    return true;
  }
  if (msg == "ON" || msg == "1" || msg == "TRUE") {
    *outState = true;
    return true;
  }
  if (msg == "OFF" || msg == "0" || msg == "FALSE") {
    *outState = false;
    return true;
  }
  return false;
}

void onMqttMessage(char* topic, byte* payload, unsigned int length) {
  String msg;
  for (unsigned int i = 0; i < length; i++) {
    msg += (char)payload[i];
  }

  bool state = false;
  bool parsed = parseOnOff(msg, &state);
  bool hasBeep = msg.indexOf("BEEP") >= 0;

  if (String(topic) == TOPIC_CMD_LED) {
    if (parsed) {
      setLed(state);
      Serial.print("LED: ");
      Serial.println(state ? "ON" : "OFF");
    } else {
      Serial.println("Comando LED desconhecido.");
    }
  }

  if (String(topic) == TOPIC_CMD_BUZZER) {
    if (hasBeep) {
      setBuzzer(true);
      delay(200);
      setBuzzer(false);
      Serial.println("BUZZER: BEEP");
    } else if (parsed) {
      setBuzzer(state);
      Serial.print("BUZZER: ");
      Serial.println(state ? "ON" : "OFF");
    } else {
      Serial.println("Comando BUZZER desconhecido.");
    }
  }
}

float simulatedTemperature() {
  return 24.0 + (random(-30, 30) / 10.0);
}

float simulatedHumidity() {
  return 55.0 + (random(-150, 150) / 10.0);
}

int simulatedLight() {
  return random(200, 900);
}

void publishTelemetry(float tempC, float humidity, int lightRaw, const char* mode) {
  char tempStr[10];
  char humStr[10];
  dtostrf(tempC, 1, 1, tempStr);
  dtostrf(humidity, 1, 1, humStr);

  char payload[256];

  snprintf(payload, sizeof(payload),
           "{\"device_id\":\"%s\",\"ts\":%lu,\"value\":%s,\"unit\":\"C\",\"mode\":\"%s\"}",
           DEVICE_ID, millis(), tempStr, mode);
  client.publish(TOPIC_TELEMETRY_TEMP, payload);

  snprintf(payload, sizeof(payload),
           "{\"device_id\":\"%s\",\"ts\":%lu,\"value\":%s,\"unit\":\"pct\",\"mode\":\"%s\"}",
           DEVICE_ID, millis(), humStr, mode);
  client.publish(TOPIC_TELEMETRY_HUM, payload);

  snprintf(payload, sizeof(payload),
           "{\"device_id\":\"%s\",\"ts\":%lu,\"value\":%d,\"unit\":\"raw\",\"mode\":\"%s\"}",
           DEVICE_ID, millis(), lightRaw, mode);
  client.publish(TOPIC_TELEMETRY_LIGHT, payload);
}

void publishStatus(const char* mode) {
  String ip = WiFi.localIP().toString();
  char payload[256];
  snprintf(payload, sizeof(payload),
           "{\"device_id\":\"%s\",\"ts\":%lu,\"wifi_rssi\":%ld,\"ip\":\"%s\",\"mqtt_connected\":%s,\"mode\":\"%s\"}",
           DEVICE_ID, millis(), (long)WiFi.RSSI(), ip.c_str(), client.connected() ? "true" : "false", mode);
  client.publish(TOPIC_STATUS, payload);
}

void publishAlert(const char* message) {
  char payload[256];
  snprintf(payload, sizeof(payload),
           "{\"device_id\":\"%s\",\"ts\":%lu,\"level\":\"warn\",\"message\":\"%s\"}",
           DEVICE_ID, millis(), message);
  client.publish(TOPIC_ALERT, payload);
}

void setup() {
  Serial.begin(115200);
  delay(300);
  randomSeed(micros());

  pinMode(LED_PIN, OUTPUT);
  pinMode(BUZZER_PIN, OUTPUT);
  setLed(false);
  setBuzzer(false);

  if (dhtEnabled) {
    dht.begin();
  }

  setupWiFi();
  client.setServer(MQTT_HOST, MQTT_PORT);
  client.setCallback(onMqttMessage);
}

void loop() {
  ensureWiFi();

  if (!client.connected()) {
    connectMqtt();
  }
  client.loop();

  unsigned long now = millis();
  if (now - lastPublish >= PUBLISH_INTERVAL_MS) {
    lastPublish = now;

    bool simulated = false;
    float tempC = NAN;
    float humidity = NAN;

    if (dhtEnabled) {
      tempC = dht.readTemperature();
      humidity = dht.readHumidity();
    }

    if (isnan(tempC) || isnan(humidity)) {
      simulated = true;
      tempC = simulatedTemperature();
      humidity = simulatedHumidity();
    }

    int lightRaw = 0;
    if (ldrEnabled) {
      lightRaw = analogRead(LDR_PIN);
      if (lightRaw == 0) {
        simulated = true;
        lightRaw = simulatedLight();
      }
    } else {
      simulated = true;
      lightRaw = simulatedLight();
    }

    const char* mode = simulated ? "simulated" : "sensor";

    publishTelemetry(tempC, humidity, lightRaw, mode);
    publishStatus(mode);

    if (tempC > 30.0) {
      publishAlert("temperatura acima do esperado");
    }
    if (humidity < 35.0 || humidity > 75.0) {
      publishAlert("umidade fora da faixa esperada");
    }

    Serial.print("Temp: ");
    Serial.print(tempC);
    Serial.print(" C | Hum: ");
    Serial.print(humidity);
    Serial.print(" % | Light: ");
    Serial.println(lightRaw);
  }
}
