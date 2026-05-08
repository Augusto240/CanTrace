# Plano de Prototipagem no Tinkercad

## Objetivo da simulacao
Validar a logica de leitura de sensores e acionamento de atuadores em um ambiente controlado, antes da montagem fisica.

## Componentes sugeridos para a simulacao
- Microcontrolador compativel no Tinkercad.
- Sensor DHT (ou equivalente de simulacao).
- LDR + resistor.
- LED + resistor.
- Buzzer.
- Protoboard e jumpers.

## Descricao do circuito
Ligacao do DHT em pino digital, LDR em entrada analogica com divisor de tensao, LED e buzzer em pinos digitais com resistores adequados.

## Entradas
- Temperatura e umidade (DHT).
- Luminosidade (LDR).

## Saidas
- LED para indicacao visual.
- Buzzer para alerta sonoro simples.

## Logica simulada
- Leitura periodica dos sensores.
- Atualizacao de valores no Serial Monitor.
- Acionamento de LED e buzzer por condicao simulada.

## Limitacoes do Tinkercad
O Tinkercad nao simula Wi-Fi/MQTT. A validacao de comunicacao deve ser demonstrada no hardware fisico ou em simulacao separada.

## Como explicar isso na apresentacao
Informar que a simulacao valida a logica local e a parte MQTT sera demonstrada no hardware fisico e no Node-RED.

## Checklist de prints
- Circuito montado.
- Simulacao em execucao.
- Leituras no Serial Monitor.
