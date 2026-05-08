# Plano de Apresentacao - CanTrace IoT Station

## Roteiro (ate 30 minutos)
| Etapa | Tempo estimado | Conteudo |
| --- | --- | --- |
| Introducao | 3 min | Contexto academico e objetivo do modulo IoT. |
| Problema real | 3 min | Necessidade de evidencias e monitoramento ambiental. |
| Arquitetura | 5 min | Fluxos MQTT e componentes principais. |
| Tinkercad | 4 min | Simulacao, logica e limitacoes. |
| Hardware | 4 min | Montagem fisica e sensores/atuadores. |
| MQTT | 4 min | Topicos, payloads e testes. |
| Node-RED | 4 min | Dashboard e comandos. |
| Demonstracao | 3 min | Telemetria e acionamento ao vivo. |

## Perguntas provaveis do professor
- Por que MQTT foi escolhido?
- Como o sistema funciona sem Wi-Fi no Tinkercad?
- Como garantir continuidade se sensores falharem?
- Por que o frontend nao conecta direto ao MQTT?
- Qual QoS foi usado e por que?

## Respostas tecnicas sugeridas
- MQTT e leve, padronizado e adequado para telemetria IoT.
- O Tinkercad valida a logica local; a comunicacao e demonstrada no hardware fisico e no Node-RED.
- O firmware possui fallback com dados simulados quando sensores falham.
- O frontend do produto final integra via backend por seguranca e separacao de responsabilidades.
- QoS 0 para telemetria/status/alertas e QoS 1 para comandos.
