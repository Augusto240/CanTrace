# FRONTEND_GAP_ANALYSIS.md

> **Auditoria Completa — B5 Frontend CanTrace**
> **Data:** 2026-06-15
> **Nota do Usuário:** 5.5/10
> **Classificação:** FRONTAL DE RECOVERY

---

## Resumo Executivo

O frontend B5 é um **shell visual** — tem uma UI atraente com tema SCADA dark, mas por trás da superfície, **todos os dados são hardcoded**, não existe integração com autenticação, não existem páginas específicas, não existe data binding real, e há uma omissão crítica na configuração `provideHttpClient()` que impediria até mesmo a única chamada API implementada de funcionar.

---

## 1. Navegação & Rotas

| Item | Status | Severidade |
|------|--------|------------|
| Sidebar com 5 itens de navegação | ✅ Visual | — |
| Rotas definidas para 5 caminhos | ⚠️ Placeholder | ALTO |
| **Todas as rotas carregam o MESMO componente** | ❌ Broken | CRÍTICO |
| Componentes de página dedicados (Dispositivos, Telemetria, Alertas, Auditoria) | ❌ Ausente | CRÍTICO |
| Rota 404 / página não encontrada | ❌ Ausente | MÉDIO |

**Detalhes:**
- `/dashboard`, `/dispositivos`, `/telemetria`, `/alertas`, `/auditoria` → todos carregam `DashboardPageComponent`
- Não existem `DispositivosPageComponent`, `TelemetriaPageComponent`, `AlertasPageComponent`, `AuditoriaPageComponent`
- A navegação visual funciona (links, estado ativo), mas todo link renderiza a mesma tela

---

## 2. Autenticação

| Item | Status | Severidade |
|------|--------|------------|
| Login page/component | ❌ Ausente | CRÍTICO |
| Auth service (token/session) | ❌ Ausente | CRÍTICO |
| Logout mechanism | ❌ Ausente | CRÍTICO |
| User model | ❌ Ausente | ALTO |
| Environment files (API base URL) | ❌ Ausente | ALTO |
| Proxy configuration (dev server) | ❌ Ausente | ALTO |

**Detalhes:**
- Backend usa HTTP Basic Auth com dois usuários: `admin/admin123` (ADMIN) e `operator/operator123` (OPERATOR)
- Frontend tem ZERO integração com este sistema
- Perfil de usuário no sidebar é hardcoded: `"Admin"` / `"admin@cantrace"`

---

## 3. Guards (Auth/Role)

| Item | Status | Severidade |
|------|--------|------------|
| AuthGuard | ❌ Ausente | CRÍTICO |
| RoleGuard | ❌ Ausente | ALTO |
| Route protection | ❌ Ausente | CRÍTICO |

**Detalhes:**
- Nenhuma rota é protegida — todas são publicamente acessíveis
- Backend tem role-based access control (ADMIN vs OPERATOR) mas frontend não enforce nem detecta

---

## 4. HTTP Interceptor

| Item | Status | Severidade |
|------|--------|------------|
| HttpInterceptor | ❌ Ausente | CRÍTICO |
| `provideHttpClient()` no app config | ❌ Ausente | CRÍTICO |
| Auth credentials attach | ❌ Ausente | CRÍTICO |

**Detalhes:**
- `provideHttpClient()` **NÃO** está configurado em `app.config.ts`
- `DashboardService` injeta `HttpClient`, mas o app nunca o provê
- **Bug crítico:** Qualquer chamada HTTP causará erro de injeção em runtime
- Sem interceptor, não há mecanismo para anexar credenciais HTTP Basic Auth

---

## 5. Chamadas API Reais

| Item | Status | Severidade |
|------|--------|------------|
| DashboardService → GET /api/v1/dashboard | ⚠️ Implementado mas quebrado | CRÍTICO |
| DispositivoService → /api/v1/dispositivos | ❌ Ausente | CRÍTICO |
| TelemetriaService → /api/v1/telemetria | ❌ Ausente | ALTO |
| AlertaService → /api/v1/alertas | ❌ Ausente | ALTO |
| AuditService → /api/v1/auditoria | ❌ Ausente | MÉDIO |
| LoteService → /api/v1/lotes | ❌ Ausente | BAIXO |

**Detalhes:**
- Apenas 2 services existem: `dashboard.service.ts` e `websocket.service.ts`
- Backend tem APIs REST completas para Dispositivos, Telemetria, Alertas, Auditoria, Lotes
- Nenhum service frontend consome estas APIs

---

## 6. Login/Logout

| Item | Status | Severidade |
|------|--------|------------|
| Login page | ❌ Ausente | CRÍTICO |
| Login form | ❌ Ausente | CRÍTICO |
| Login service | ❌ Ausente | CRÍTICO |
| Logout button | ❌ Ausente | CRÍTICO |
| Logout function | ❌ Ausente | CRÍTICO |
| Session clearing | ❌ Ausente | CRÍTICO |

---

## 7. Dashboard Data Flow

| Item | Status | Severidade |
|------|--------|------------|
| DashboardService.fetch() | ✅ Implementado | — |
| Dados chegando ao DashboardPage | ❌ Broken | CRÍTICO |
| Dados passados para widgets | ❌ Broken | CRÍTICO |
| Topbar "Atualizar" re-fetch | ❌ Broken | ALTO |
| Topbar MQTT status dinâmico | ❌ Hardcoded | MÉDIO |

**Detalhes:**
- `DashboardPageComponent` chama `dashboardService.getDashboard()` em `ngOnInit`, mas a subscription **não faz nada com os dados** — `.subscribe()` sem callback
- Chama `websocketService.connect()` mas nunca assina `telemetry$` ou `alerts$`
- **Nenhum dado é passado para nenhum widget** — todos renderizados sem bindings `[input]`
- Topbar "Atualizar" apenas atualiza timestamp local — NÃO re-fetch dos dados
- Topbar `mqttConnected` é hardcoded `true` — nunca reflete status real

---

## 8. WebSocket Integration

| Item | Status | Severidade |
|------|--------|------------|
| WebSocketService implementado | ✅ Implementado | — |
| Conexão WebSocket | ⚠️ Chamado mas não consumido | ALTO |
| Eventos telemetry$ | ❌ Não consumido | CRÍTICO |
| Eventos alerts$ | ❌ Não consumido | CRÍTICO |
| Reconexão automática | ✅ Implementado | — |

**Detalhes:**
- `WebSocketService` é bem estruturado com reconexão exponential backoff
- `DashboardPageComponent` chama `connect()` no init e `disconnect()` no destroy
- **Gap crítico:** Ninguém assina `telemetry$` ou `alerts$` — eventos são parseados e emitidos mas nunca consumidos
- Widget de eventos MQTT e widget de alertas nunca atualizam via WebSocket

---

## 9. Widget Components

| Widget | Data Source | Status |
|--------|-------------|--------|
| **AlertasWidgetComponent** | `@Input()` defaults: criticos=3, medios=7, baixos=12 | ❌ HARDCODED |
| **EventosMqttWidgetComponent** | `@Input()` defaults: 5 eventos hardcoded | ❌ HARDCODED |
| **MapaOperacionalWidgetComponent** | `@Input()` defaults: 4 pins hardcoded | ❌ HARDCODED |
| **SaudeSistemaWidgetComponent** | Array local: todos "online" | ❌ HARDCODED |
| **TimelineEventosWidgetComponent** | `@Input()` defaults: 5 eventos hardcoded | ❌ HARDCODED |

**Detalhes:**
- Nenhum widget injeta service, assina Observable, ou recebe dados do pai
- Todos são componentes de display puro com dados estáticos
- `SkeletonLoaderComponent` e `StatusBadgeComponent` existem mas nunca são usados

---

## 10. Tela Vazias / Placeholders

| Item | Status | Severidade |
|------|--------|------------|
| `app.html` — 342 linhas de placeholder Angular | ❌ Dead code | MÉDIO |
| Sidebar user profile — hardcoded | ❌ Placeholder | MÉDIO |
| Todas as 5 rotas → mesmo componente | ❌ Placeholder | CRÍTICO |

---

## 11. Features Ausentes

### Críticos (Must Have)
1. Login page/component
2. Logout functionality
3. AuthGuard — proteger rotas
4. HttpInterceptor — anexar credenciais
5. `provideHttpClient()` no app config
6. Dispositivos page (CRUD completo)
7. Telemetria page (histórico com filtros)
8. Alertas page (listar, resolver, ignorar)
9. Auditoria page (logs + stats)
10. Device service — integração API
11. Telemetry service — integração API
12. Alerts service — integração API
13. Audit service — integração API
14. Environment files
15. Proxy configuration

### Altos (Should Have)
16. Mobile responsive sidebar
17. Real-time widget updates via WebSocket
18. Topbar动态 status
19. Dynamic alert badges
20. Error handling / error pages (404)
21. Loading states

### Médios (Nice to Have)
22. Skeleton loader usage
23. Status badge usage
24. Dark/light theme toggle
25. Data refresh on WebSocket events

---

## 12. Issues Críticos

### Runtime-Breaking
1. **`provideHttpClient()` ausente em `app.config.ts`** — DashboardService injeta HttpClient mas app nunca provê. Erro de injeção em runtime.

### Arquiteturais
2. **Todas as rotas apontam para o mesmo componente** — 5 links sidebar, 5 rotas, 1 componente
3. **Zero data flow services → widgets** — DashboardService busca dados mas nada é passado para componentes filhos
4. **WebSocket events são fire-and-forget** — Eventos parseados mas nunca consumidos
5. **Sem camada de autenticação** — Backend enforce HTTP Basic mas frontend não consegue autenticar

### Code Quality
6. **Dead file: `app.html`** — 342 linhas de placeholder nunca usadas
7. **Hardcoded values everywhere** — Badge sidebar (3), MQTT status (true), user profile ("Admin"), todos widgets
8. **Sem error handling nas subscriptions** — getDashboard() sem error callback
9. **SkeletonLoader e StatusBadge orphaned** — Construídos mas nunca usados

---

## Conclusão

**O frontend é essencialmente um mockup visual.** Tem uma UI SCADA dark atraente, mas:
- Zero integração com backend
- Zero autenticação
- Zero data binding real
- Todas as rotas carregam a mesma tela
- Todos os widgets mostram dados hardcoded
- Bug crítico impede qualquer chamada HTTP

**Nota do usuário: 5.5/10** — Reflete o polish visual do shell enquanto reconhece a ausência completa de funcionalidade.
