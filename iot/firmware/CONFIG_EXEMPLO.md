# Configuracao de Exemplo - Firmware (ESP32, modo simulado)

## Versao minima (ESP32 + LED interno)
Esta versao usa apenas o ESP32 e o LED interno no GPIO 2.
Sensores fisicos sao opcionais para evolucao futura.

## Onde alterar SSID e senha
No inicio do arquivo cantrace_iot_station.ino:
```cpp
const char* WIFI_SSID = "SEU_SSID";
const char* WIFI_PASSWORD = "SUA_SENHA";
```

## Onde alterar o broker Mosquitto
```cpp
const char* MQTT_HOST = "SEU_BROKER_IP";
const uint16_t MQTT_PORT = 1883;
```
Use o IPv4 do computador que roda o Mosquitto (ex.: 10.209.1.50).
Nao use 127.0.0.1 no ESP32, pois ele aponta para o proprio microcontrolador.

## Como descobrir o IP do computador (Windows)
1. Abra o Prompt de Comando.
2. Execute: ipconfig
3. Copie o "Endereco IPv4" do adaptador em uso.

## Pinos sugeridos (ESP32)
- LED interno: GPIO 2.
- Buzzer (opcional): GPIO 15.
- DHT (opcional): GPIO 4.
- LDR (opcional): GPIO 34 (ADC1).

## Bibliotecas necessarias
- WiFi.h (ja vem com o core do ESP32).
- PubSubClient.

## Bibliotecas opcionais (para sensores futuros)
- DHT sensor library (Adafruit).
- Adafruit Unified Sensor.

## Como compilar na Arduino IDE
1. Instalar o core "ESP32 by Espressif Systems" no Boards Manager.
2. Selecionar a placa (ex.: "ESP32 Dev Module").
3. Selecionar a porta serial.
4. Compilar e fazer Upload.
