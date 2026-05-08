# CanTrace IoT Station

## Objetivo
Fornecer uma base demonstravel de monitoramento ambiental com telemetria via MQTT e visualizacao em Node-RED, usando dados ficticios ou simulados para fins academicos.

## Arquitetura resumida
Sensores -> ESP32/NodeMCU -> Mosquitto -> Node-RED -> Dashboard
Dashboard -> Mosquitto -> ESP32/NodeMCU -> Atuadores

## Estrutura do modulo
- firmware/: codigo .ino, configuracao e instrucoes.
- node-red/: flows e orientacoes de dashboard.
- tinkercad/: plano e evidencias de simulacao.
- evidencias/: prints, fotos e video da demonstracao.

## Como testar (visao geral)
1. Subir o broker Mosquitto local.
2. Compilar e carregar o firmware no ESP32/NodeMCU.
3. Importar o flow no Node-RED e configurar o broker.
4. Verificar telemetria no dashboard e enviar comandos.

## Relacao com o CanTrace
O modulo IoT gera evidencias tecnicas que complementam a governanca documental do CanTrace. O produto final nao conecta o frontend diretamente ao MQTT.

## Status
Base tecnica e documental pronta para prototipagem, montagem fisica e demonstracao da disciplina.
