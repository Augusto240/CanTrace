# Node-RED

Este diretorio contem o flow inicial do dashboard academico do CanTrace IoT Station.

## Como importar o flow
1. Abrir o Node-RED.
2. Menu -> Import -> Clipboard.
3. Colar o conteudo de flows.example.json.
4. Fazer Deploy.

## Configurar o broker MQTT
- Ajustar o node de broker para o IP/host do Mosquitto (ex.: 127.0.0.1:1883).
- Garantir que os topicos estejam com o prefixo cantrace/area-01/.
Para o ESP32, use o IPv4 do computador (ipconfig) no firmware; 127.0.0.1 vale apenas para o Node-RED local.

## Payloads e comandos
- O flow espera JSON com campo value para telemetria simulada.
- O LED e o atuador principal; buzzer e opcional.
- Comandos de LED e buzzer sao enviados em JSON com campo state.

## Testar recebimento de telemetria
```bash
mosquitto_sub -h 127.0.0.1 -t "cantrace/area-01/#" -v
```
Verifique a chegada das mensagens no debug e nos widgets do dashboard.

## Testar envio de comandos
Use os botoes e switches do dashboard para publicar em:
- cantrace/area-01/command/led
- cantrace/area-01/command/buzzer

## Observacao
Se o Node-RED Dashboard estiver em outra versao, alguns widgets podem exigir ajustes de layout ou tamanho.
Para ESP32, o LDR pode variar ate 4095; ajuste o maximo do gauge se necessario.
