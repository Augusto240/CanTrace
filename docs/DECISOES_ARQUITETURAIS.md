# Decisoes Arquiteturais - CanTrace

## Registro
Data de referencia: 2026-05-08.

## Decisoes tomadas

### D01 - Backend com Spring Boot
- Decisao: utilizar Java com Spring Boot, Spring Security e Spring Data JPA.
- Justificativa: maturidade do ecossistema, padroes corporativos e suporte a seguranca.
- Status: aprovada.

### D02 - Banco de dados PostgreSQL
- Decisao: utilizar PostgreSQL como banco relacional principal.
- Justificativa: robustez, padrao academico e ampla compatibilidade.
- Status: aprovada.

### D03 - Frontend com Angular
- Decisao: utilizar Angular com TypeScript.
- Justificativa: padrao corporativo, organizacao modular e tooling maduro.
- Status: aprovada.

### D04 - MQTT com Mosquitto
- Decisao: utilizar MQTT com broker Mosquitto para o modulo IoT.
- Justificativa: protocolo leve e adequado para telemetria.
- Status: aprovada.

### D05 - Docker Compose para infraestrutura
- Decisao: utilizar Docker e Docker Compose na infraestrutura futura.
- Justificativa: reprodutibilidade e isolamento de servicos.
- Status: aprovada.

### D06 - Frontend nao conecta diretamente ao MQTT
- Decisao: o frontend nao consumira MQTT diretamente no produto final.
- Justificativa: separacao de responsabilidades e seguranca.
- Status: aprovada.

### D07 - Node-RED no modulo pratico de IoT
- Decisao: utilizar Node-RED para o dashboard academico de IoT.
- Justificativa: rapidez de prototipacao e aderencia a disciplina.
- Status: aprovada.

### D08 - Sistema exclusivamente web
- Decisao: nao ha plano de aplicativo mobile para o produto.
- Justificativa: foco no escopo academico e simplificacao do MVP.
- Status: aprovada.
