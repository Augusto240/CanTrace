# Plano de Prototipagem no Tinkercad

## Objetivo da simulacao
Validar a logica de entrada/saida e o acionamento de atuadores em um ambiente controlado, antes da demonstracao com ESP32.

## Componentes sugeridos para a simulacao
- Microcontrolador compativel no Tinkercad.
- LED + resistor.
- Buzzer (opcional).
- Protoboard e jumpers.

## Descricao do circuito
Ligacao de LED e buzzer em pinos digitais com resistores adequados. As entradas ambientais sao simuladas no codigo.

## Entradas
- Temperatura, umidade e luminosidade simuladas no codigo.

## Saidas
- LED para indicacao visual.
- Buzzer para alerta sonoro simples (opcional).

## Logica simulada
- Geracao de valores simulados.
- Atualizacao de valores no Serial Monitor.
- Acionamento de LED e buzzer por condicao simulada.

## Limitacoes do Tinkercad
O Tinkercad nao simula Wi-Fi/MQTT. A validacao de comunicacao deve ser demonstrada no hardware fisico ou em simulacao separada.

## Como explicar isso na apresentacao
Informar que a simulacao valida a logica local e a parte MQTT sera demonstrada no ESP32 real e no Node-RED.

## Checklist de prints
- Circuito montado.
- Simulacao em execucao.
- Leituras no Serial Monitor.
