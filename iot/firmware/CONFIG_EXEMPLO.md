# Configuracao de Exemplo - Firmware

## Onde alterar SSID e senha
No inicio do arquivo cantrace_iot_station.ino:
```cpp
const char* WIFI_SSID = "SEU_SSID";
const char* WIFI_PASSWORD = "SUA_SENHA";
```

## Onde alterar o broker Mosquitto
```cpp
const char* MQTT_HOST = "192.168.0.10";
const uint16_t MQTT_PORT = 1883;
```

## Pinos sugeridos
- ESP32: DHT=4, LDR=34, LED=2, BUZZER=15.
- NodeMCU: DHT=D2 (GPIO 4), LDR=A0, LED=LED_BUILTIN, BUZZER=D1 (GPIO 5).

## Como instalar bibliotecas
1. Arduino IDE -> Sketch -> Include Library -> Manage Libraries.
2. Instalar: PubSubClient.
3. Instalar: DHT sensor library (Adafruit).
4. Instalar: Adafruit Unified Sensor (dependencia do DHT).

## Como compilar na Arduino IDE
1. Selecionar a placa correta (ESP32 ou ESP8266).
2. Selecionar a porta serial.
3. Compilar e fazer Upload.

## Diferencas entre ESP32 e NodeMCU
- ESP32: usar o core "ESP32 by Espressif Systems" no Boards Manager; ADC 0-4095; LED interno geralmente ativo em nivel alto.
- NodeMCU (ESP8266): usar o core "ESP8266 by ESP8266 Community"; ADC 0-1023; LED interno geralmente ativo em nivel baixo.
