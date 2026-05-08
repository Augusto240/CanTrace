# CanTrace IoT Station

## Objetivo
Coletar e publicar dados ambientais simulados para fins de rastreabilidade e auditoria academica, utilizando MQTT e um dashboard dedicado.

## Relacao com o CanTrace
O modulo IoT fornece evidencias tecnicas que complementam a governanca documental do CanTrace, sem conexao direta do frontend com MQTT.

## Sensores previstos
- Temperatura.
- Umidade relativa.
- CO2 (simulado).
- Luminosidade (simulada).
- Pressao atmosferica (opcional).

## Atuadores previstos
- LED ou buzzer para alertas simulados.
- Rele para acionamento demonstrativo (quando aplicavel).

## Tinkercad
Simulacoes de circuito e sensores para validacao academica dos fluxos de dados.

## Hardware fisico
Montagem fisica opcional com ESP32/NodeMCU, de acordo com disponibilidade.

## MQTT
Publicacao em topicos padronizados com payloads JSON e timestamps.

## Node-RED
Dashboard academico para visualizacao de metricas e evidencias.
