# FRONTEND_RECOVERY_PLAN.md

> **Plano de Recuperação — B5 Frontend CanTrace**
> **Data:** 2026-06-15
> **Objetivo:** Transformar shell visual em Environmental Operations Center funcional
> **Nota Atual:** 5.5/10 → **Meta:** 9.0/10

---

## Fase 0: Corretivos Críticos (IMEDIATO)

### Task 0.1: Corrigir `provideHttpClient()` no app.config

**Arquivo:** `frontend/src/app/app.config.ts`

```typescript
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([])),
  ]
};
```

**Verificação:** `ng build` deve compilar sem erros de injeção

---

### Task 0.2: Remover `app.html` dead code

**Arquivo:** `frontend/src/app/app.html` — DELETAR

**Verificação:** Build não deve quebrar (AppComponent usa template inline)

---

### Task 0.3: Criar environment files

**Arquivo:** `frontend/src/environments/environment.ts`

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080'
};
```

**Arquivo:** `frontend/src/environments/environment.prod.ts`

```typescript
export const environment = {
  production: true,
  apiUrl: '/api'
};
```

---

### Task 0.4: Criar proxy configuration

**Arquivo:** `frontend/proxy.conf.json`

```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true
  }
}
```

**Arquivo:** `frontend/angular.json` — adicionar proxyConfig

```json
"serve": {
  "options": {
    "proxyConfig": "proxy.conf.json"
  }
}
```

---

## Fase 1: Autenticação & Segurança

### Task 1.1: Criar Auth Service

**Arquivo:** `frontend/src/app/services/auth.service.ts`

- `login(username, password)` → POST com Basic Auth
- `logout()` → limpar sessão
- `isAuthenticated()` → verificar status
- `getUser()` → retornar usuário atual
- `getToken()` → retornar credenciais base64
- Armazenar em `sessionStorage`

---

### Task 1.2: Criar Login Page

**Arquivo:** `frontend/src/app/pages/login/login.component.ts`

- Formulário com username + password
- Chamar `authService.login()`
- Redirecionar para `/dashboard` em sucesso
- Mostrar erro em falha
- Design SCADA dark consistente

---

### Task 1.3: Criar AuthGuard

**Arquivo:** `frontend/src/app/guards/auth.guard.ts`

- Verificar `authService.isAuthenticated()`
- Redirecionar para `/login` se não autenticado
- Aplicar a todas as rotas exceto `/login`

---

### Task 1.4: Criar HttpInterceptor

**Arquivo:** `frontend/src/app/interceptors/auth.interceptor.ts`

- Interceptar todas as requisições HTTP
- Anexar header `Authorization: Basic <credentials>`
- Tratar 401 → redirecionar para login

---

### Task 1.5: Criar Logout

**Arquivo:** Sidebar component — adicionar botão logout

- Chamar `authService.logout()`
- Redirecionar para `/login`
- Limpar sessionStorage

---

## Fase 2: Services API

### Task 2.1: Criar DispositivoService

**Arquivo:** `frontend/src/app/services/dispositivo.service.ts`

```typescript
// Endpoints:
GET    /api/v1/dispositivos          → listar todos
GET    /api/v1/dispositivos/:id      → buscar por ID
POST   /api/v1/dispositivos          → criar
PUT    /api/v1/dispositivos/:id      → atualizar
DELETE /api/v1/dispositivos/:id      → deletar
PUT    /api/v1/dispositivos/:id/status → mudar status
```

---

### Task 2.2: Criar TelemetriaService

**Arquivo:** `frontend/src/app/services/telemetria.service.ts`

```typescript
// Endpoints:
GET /api/v1/telemetria                    → listar leituras
GET /api/v1/telemetria/dispositivo/:id    → por dispositivo
GET /api/v1/telemetria/estatisticas       → stats
```

---

### Task 2.3: Criar AlertaService

**Arquivo:** `frontend/src/app/services/alerta.service.ts`

```typescript
// Endpoints:
GET    /api/v1/alertas                  → listar
GET    /api/v1/alertas/:id             → buscar por ID
PUT    /api/v1/alertas/:id/resolver    → resolver
PUT    /api/v1/alertas/:id/ignorar     → ignorar
```

---

### Task 2.4: Criar AuditService

**Arquivo:** `frontend/src/app/services/audit.service.ts`

```typescript
// Endpoints:
GET /api/v1/auditoria                   → listar logs
GET /api/v1/auditoria/stats             → estatísticas
```

---

## Fase 3: Páginas de Navegação

### Task 3.1: Criar DispositivosPage

**Arquivo:** `frontend/src/app/pages/dispositivos/dispositivos-page.component.ts`

- Tabela de dispositivos com status
- Botão criar novo dispositivo
- Ações: editar, deletar, mudar status
- Skeleton loader durante carregamento
- Status badges para cada dispositivo

---

### Task 3.2: Criar TelemetriaPage

**Arquivo:** `frontend/src/app/pages/telemetria/telemetria-page.component.ts`

- Filtros: dispositivo, período, tipo de leitura
- Gráficos de temperatura, umidade, luminosidade
- Tabela de leituras recentes
- Atualização em tempo real via WebSocket

---

### Task 3.3: Criar AlertasPage

**Arquivo:** `frontend/src/app/pages/alertas/alertas-page.component.ts`

- Lista de alertas com severity (crítico, médio, baixo)
- Ações: resolver, ignorar
- Filtros por status e severidade
- Contadores de alertas ativos

---

### Task 3.4: Criar AuditoriaPage

**Arquivo:** `frontend/src/app/pages/auditoria/auditoria-page.component.ts`

- Timeline de eventos de auditoria
- Filtros por tipo, período, usuário
- Detalhes expandíveis
- Stats de auditoria

---

### Task 3.5: Criar 404 Page

**Arquivo:** `frontend/src/app/pages/not-found/not-found-page.component.ts`

- Página de "Não encontrado"
- Link voltar ao dashboard
- Design SCADA consistente

---

## Fase 4: Dashboard com Dados Reais

### Task 4.1: Atualizar DashboardPageComponent

- Injetar DashboardService, WebSocketService
- Assinar `dashboard$` e passar dados para widgets
- Assinar `telemetry$` e `alerts$` do WebSocket
- Atualizar widgets em tempo real
- Adicionar loading states
- Adicionar error handling

---

### Task 4.2: Atualizar AlertasWidgetComponent

- Receber dados do DashboardService via `@Input()`
- Mostrar contadores reais de alertas
- Último alerta real (não hardcoded)
- Atualização via WebSocket

---

### Task 4.3: Atualizar SaudeSistemaWidgetComponent

- Verificar status real dos serviços via API
- Latência real do broker MQTT
- Uptime real da API
- Conexões WebSocket ativas

---

### Task 4.4: Atualizar EventosMqttWidgetComponent

- Consumir `telemetry$` do WebSocketService
- Mostrar eventos em tempo real
- Formatação monospace estilo terminal
- Auto-scroll para novos eventos

---

### Task 4.5: Atualizar MapaOperacionalWidgetComponent

- Buscar dispositivos reais via DispositivoService
- Posições baseadas em dados reais
- Status real de cada dispositivo
- Atualização via WebSocket

---

### Task 4.6: Atualizar TimelineEventosWidgetComponent

- Consumir `alerts$` do WebSocketService
- Timeline de eventos reais
- Cores baseadas em severidade
- Auto-atualização

---

### Task 4.7: Atualizar TopBarComponent

- Status MQTT real via `websocketService.status$`
- Última atualização real
- Botão "Atualizar" deve re-fetch dos dados
- Título dinâmico baseado na rota

---

## Fase 5: UX & Polish

### Task 5.1: Loading States

- Skeleton loaders em todas as páginas
- Spinner durante chamadas API
- Transições suaves

---

### Task 5.2: Error Handling

- Toast notifications para erros
- Retry automático em falhas de rede
- Página de erro dedicada

---

### Task 5.3: Mobile Responsive

- Sidebar collapsible em mobile
- Hamburger menu
- Grid responsivo nos widgets

---

### Task 5.4: Alert Badges Dinâmicos

- Badge no sidebar baseado em alertas reais
- Atualização em tempo real via WebSocket

---

## Ordem de Execução

```
Fase 0 (Corretivos Críticos) → Fase 1 (Auth) → Fase 2 (Services) →
Fase 3 (Páginas) → Fase 4 (Dashboard) → Fase 5 (UX)
```

**Cada fase deve ser completada e verificada antes de avançar.**

---

## Critérios de Aceite

### Fase 0
- [ ] `ng build` sem erros
- [ ] `ng test` passa
- [ ] Dashboard carrega sem erros no console

### Fase 1
- [ ] Login funcional com credenciais reais
- [ ] Logout limpa sessão
- [ ] Rotas protegidas redirecionam para login
- [ ] Credenciais anexadas a todas as chamadas API

### Fase 2
- [ ] Todos os services implementados
- [ ] Services testados com mocks

### Fase 3
- [ ] Todas as 5 páginas funcionais
- [ ] Navegação funciona entre todas as páginas
- [ ] 404 page funcional

### Fase 4
- [ ] Dashboard mostra dados reais da API
- [ ] Widgets atualizam via WebSocket
- [ ] Topbar mostra status real
- [ ] Botão "Atualizar" funciona

### Fase 5
- [ ] Loading states em todas as páginas
- [ ] Error handling funcional
- [ ] Mobile responsive
- [ ] Alert badges dinâmicos

---

## Estimativa de Esforço

| Fase | Tasks | Estimativa |
|------|-------|------------|
| Fase 0 | 4 | 30 min |
| Fase 1 | 5 | 2-3 horas |
| Fase 2 | 4 | 2 horas |
| Fase 3 | 5 | 4-5 horas |
| Fase 4 | 7 | 4-5 horas |
| Fase 5 | 4 | 2-3 horas |
| **Total** | **29** | **15-19 horas** |

---

## Nota Final

Após implementação completa deste plano, o frontend deve atingir **9.0/10** — um Environmental Operations Center funcional com:
- Autenticação real
- Navegação funcional
- Dados reais da API
- Atualização em tempo real via WebSocket
- Design SCADA dark profissional
- Mobile responsive
