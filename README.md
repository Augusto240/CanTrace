# CanTrace

## Descrição curta
CanTrace é uma plataforma web corporativa para uma associação fictícia autorizada, com foco em rastreabilidade regulatória, governança documental, auditoria e monitoramento ambiental via IoT/MQTT em processos regulados envolvendo Cannabis medicinal.

## Aviso de escopo e segurança jurídica
- Não realiza comercialização.
- Não faz publicidade de produtos.
- Não oferece prescrição automatizada.
- Não oferece orientação prática de cultivo.
- Não ensina germinação, plantio, colheita ou processamento.
- Utiliza apenas dados fictícios/sintéticos para fins acadêmicos.
- Mantém foco em compliance, rastreabilidade, auditoria, gestão documental e monitoramento ambiental.

## Stack definida
- Backend: Java, Spring Boot, Spring Security, Spring Data JPA.
- Banco de dados: PostgreSQL.
- Frontend: Angular, TypeScript.
- IoT: MQTT, Mosquitto.
- Dashboard IoT: Node-RED (módulo prático da disciplina).
- Infraestrutura: Docker, Docker Compose.
- Testes: JUnit 5, Mockito (backend) e Cypress (frontend).

## Estrutura de pastas
```
.
├── backend/
├── docs/
│   ├── banca/
│   ├── diagramas/
│   ├── iot/
│   └── requisitos/
├── frontend/
├── infra/
└── iot/
	├── evidencias/
	├── firmware/
	├── node-red/
	└── tinkercad/
```

## Visão geral do módulo IoT
O módulo CanTrace IoT Station é responsável por coletar métricas ambientais simuladas e publicar dados via MQTT para fins acadêmicos de rastreabilidade e auditoria. O fluxo prevê um broker Mosquitto, tópicos padronizados, payloads JSON e um dashboard no Node-RED para visualização. O frontend do produto não se conecta diretamente ao MQTT.

## Comandos básicos futuros
Os comandos abaixo serão detalhados quando os módulos forem inicializados. Nesta fase, eles não estão disponíveis.

```bash
# Backend (futuro)
./mvnw spring-boot:run
./mvnw test

# Frontend (futuro)
npm install
ng serve
npm run test

# Infraestrutura (futuro)
docker compose up -d
```

## Status atual do projeto
Fundação documental e estrutural do repositório, sem implementação de backend, frontend ou infraestrutura.
