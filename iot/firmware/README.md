# Firmware IoT

## Objetivo
Firmware base do CanTrace IoT Station para ESP32/NodeMCU, com conectividade Wi-Fi, MQTT, telemetria e comandos de atuadores.

## Dependencias
- Arduino IDE.
- Bibliotecas: PubSubClient e DHT sensor library.
- Driver/placa para ESP32 ou ESP8266.

## Pinagem sugerida
### ESP32 (ajuste conforme a placa)
- DHT: GPIO 4
- LDR: GPIO 34 (ADC1)
- LED: GPIO 2
- Buzzer: GPIO 15

### NodeMCU (ESP8266)
- DHT: D2 (GPIO 4)
- LDR: A0
- LED: LED_BUILTIN (ativo em nivel baixo)
- Buzzer: D1 (GPIO 5)

## Topicos publicados
- cantrace/area-01/telemetry/temperature
- cantrace/area-01/telemetry/humidity
- cantrace/area-01/telemetry/light
- cantrace/area-01/status
- cantrace/area-01/alert

## Topicos assinados
- cantrace/area-01/command/led
- cantrace/area-01/command/buzzer

## Formato de comando esperado
O firmware aceita comandos JSON com campo state (ON/OFF). Para buzzer, o payload pode incluir action: BEEP.

## Modo simulado
Quando sensores nao estao conectados ou a leitura falha, o firmware gera valores sinteticos para manter a demonstracao funcional.

## Modo fisico
Com sensores conectados, o firmware publica leituras reais e permite acionamento de LED e buzzer por MQTT.
