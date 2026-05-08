# Relatorio Tecnico - CanTrace IoT Station

## Titulo
Relatorio Tecnico do Modulo CanTrace IoT Station.

## Resumo
Este relatorio descreve a base tecnica do modulo IoT do projeto academico CanTrace. O objetivo e demonstrar monitoramento ambiental com dados ficticios, integracao via MQTT, visualizacao em Node-RED e evidencias para auditoria simulada, sem qualquer funcionalidade de cultivo, comercializacao ou prescricao.

## Problema real
Processos regulados exigem monitoramento ambiental e trilhas de evidencias tecnicas. Sem padronizacao, os registros podem ser incompletos, dificeis de auditar e vulneraveis a inconsistencias.

## Objetivo geral
Construir uma base funcional e demonstravel do modulo IoT para monitoramento ambiental com telemetria via MQTT e dashboard no Node-RED.

## Objetivos especificos
- Coletar dados de temperatura, umidade e luminosidade (fisicos ou simulados).
- Publicar telemetria em topicos MQTT padronizados.
- Receber comandos de atuadores via MQTT.
- Exibir informacoes em um dashboard academico.
- Registrar evidencias visuais e logs para a disciplina.

## Arquitetura da solucao
Sensores conectados ao ESP32/NodeMCU publicam dados no broker Mosquitto. O Node-RED consome a telemetria e apresenta indicadores no dashboard. Comandos do dashboard retornam ao broker e sao assinados pelo microcontrolador para acionar LED e buzzer.

## Descricao dos sensores
- DHT11 ou DHT22: temperatura e umidade relativa.
- LDR: luminosidade (leitura analogica).
- Valores simulados: fallback quando sensores nao estiverem conectados.

## Descricao dos atuadores
- LED: indicacao visual de comando remoto.
- Buzzer: alerta sonoro simples.
- Rele (opcional): demonstracao de acionamento em bancada.

## Justificativa do uso de MQTT
MQTT e leve, simples e adequado a telemetria de IoT, com baixo consumo de banda e suporte a publish/subscribe, ideal para demonstracao academica.

## Justificativa do uso do Node-RED
Node-RED acelera a prototipagem, permite dashboards visuais e integra MQTT sem necessidade de backend, atendendo ao objetivo didatico da disciplina.

## Estrutura dos topicos MQTT
- cantrace/area-01/telemetry/temperature
- cantrace/area-01/telemetry/humidity
- cantrace/area-01/telemetry/light
- cantrace/area-01/status
- cantrace/area-01/alert
- cantrace/area-01/command/led
- cantrace/area-01/command/buzzer

## Payloads JSON
Telemetria (exemplo para temperatura):
```json
{
	"device_id": "cantrace-iot-01",
	"ts": 123456,
	"value": 25.4,
	"unit": "C",
	"mode": "simulated"
}
```

Status:
```json
{
	"device_id": "cantrace-iot-01",
	"ts": 123456,
	"wifi_rssi": -56,
	"ip": "192.168.0.50",
	"mqtt_connected": true,
	"mode": "simulated"
}
```

Alerta:
```json
{
	"device_id": "cantrace-iot-01",
	"ts": 123456,
	"level": "warn",
	"message": "temperatura acima do esperado"
}
```

Comando:
```json
{
	"state": "ON",
	"source": "dashboard"
}
```

## Funcionamento esperado
O microcontrolador se conecta ao Wi-Fi, estabelece conexao com o broker MQTT e publica telemetria periodicamente. O Node-RED apresenta os dados no dashboard e permite o envio de comandos. O dispositivo assina os topicos de comando e aciona LED e buzzer conforme solicitado.

## Plano de prototipagem no Tinkercad
- Simular a leitura de sensores e acionamento de atuadores.
- Validar a logica de leitura, limites e acionamento local.
- Capturar prints do circuito e da simulacao em execucao.

## Plano de montagem fisica
- Montar ESP32/NodeMCU com DHT e LDR em protoboard.
- Conectar LED e buzzer em pinos digitais.
- Validar leituras reais e telemetria no broker local.

## Plano de demonstracao ao vivo
- Mostrar o circuito (Tinkercad ou fisico).
- Exibir publicacao de telemetria via MQTT.
- Mostrar dashboard no Node-RED com gauges e charts.
- Enviar comandos pelo dashboard e observar atuadores.

## Evidencias visuais a inserir depois
- Print do Tinkercad.
- Print do Node-RED Dashboard.
- Print do Flow no Node-RED.
- Print do Serial Monitor.
- Foto da montagem fisica (se houver).
- Video curto da demonstracao (se houver).

## Riscos e plano B
- Falta de sensores: usar valores simulados no firmware.
- Falta de hardware: demonstrar em simulacao e Node-RED.
- Instabilidade do broker: usar broker local e testes com mosquitto_pub/sub.

## Conclusao parcial
O modulo IoT possui definicao tecnica e base funcional para demonstracao. A proxima etapa consiste em validar a prototipagem, montar o hardware e registrar evidencias para a entrega final da disciplina.
