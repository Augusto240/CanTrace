import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subject, takeUntil } from 'rxjs';
import { DashboardService, DashboardData } from '../../services/dashboard.service';
import { WebSocketService, ConnectionStatus, TelemetryEvent, AlertEvent } from '../../services/websocket.service';
import { AlertasWidgetComponent } from '../alertas-widget/alertas-widget.component';
import { SaudeSistemaWidgetComponent } from '../saude-sistema-widget/saude-sistema-widget.component';
import { EventosMqttWidgetComponent } from '../eventos-mqtt-widget/eventos-mqtt-widget.component';
import { MapaOperacionalWidgetComponent } from '../mapa-operacional-widget/mapa-operacional-widget.component';
import { TimelineEventosWidgetComponent } from '../timeline-eventos-widget/timeline-eventos-widget.component';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [
    CommonModule,
    AlertasWidgetComponent,
    SaudeSistemaWidgetComponent,
    EventosMqttWidgetComponent,
    MapaOperacionalWidgetComponent,
    TimelineEventosWidgetComponent,
  ],
  template: `
    <div class="dashboard-grid" *ngIf="!loading">
      <div class="row-1">
        <app-alertas-widget
          [criticos]="criticos"
          [medios]="medios"
          [baixos]="baixos"
          [latestAlert]="latestAlert"
          [latestAlertTime]="latestAlertTime">
        </app-alertas-widget>
        <app-saude-sistema-widget></app-saude-sistema-widget>
      </div>

      <div class="row-2">
        <app-eventos-mqtt-widget [events]="mqttEvents"></app-eventos-mqtt-widget>
        <app-mapa-operacional-widget></app-mapa-operacional-widget>
      </div>

      <div class="row-3">
        <app-timeline-eventos-widget [events]="timelineEvents"></app-timeline-eventos-widget>
      </div>
    </div>
    <div class="loading-state" *ngIf="loading">
      <p>Carregando dashboard...</p>
    </div>
  `,
  styles: [`
    .dashboard-grid {
      display: flex;
      flex-direction: column;
      gap: 16px;
    }
    .row-1 {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 16px;
    }
    .row-2 {
      display: grid;
      grid-template-columns: 1fr 2fr;
      gap: 16px;
    }
    @media (max-width: 768px) {
      .row-1, .row-2 {
        grid-template-columns: 1fr;
      }
    }
    .loading-state {
      text-align: center;
      padding: 60px;
      color: #8B949E;
    }
  `]
})
export class DashboardPageComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  loading = true;
  criticos = 0;
  medios = 0;
  baixos = 0;
  latestAlert = '';
  latestAlertTime = '';
  mqttEvents: any[] = [];
  timelineEvents: any[] = [];

  constructor(
    private dashboardService: DashboardService,
    private websocketService: WebSocketService
  ) {}

  ngOnInit(): void {
    this.dashboardService.getDashboard()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data) => {
          this.baixos = data.totalAlertasAtivo;
          this.loading = false;
        },
        error: () => {
          this.loading = false;
        }
      });

    this.websocketService.connect();

    this.websocketService.telemetry$
      .pipe(takeUntil(this.destroy$))
      .subscribe(event => {
        this.addMqttEvent(event);
      });

    this.websocketService.alerts$
      .pipe(takeUntil(this.destroy$))
      .subscribe(event => {
        this.addTimelineEvent(event);
        this.latestAlert = event.titulo;
        this.latestAlertTime = 'agora';
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.websocketService.disconnect();
  }

  private addMqttEvent(event: TelemetryEvent): void {
    const mqttEvent = {
      time: new Date().toLocaleTimeString('pt-BR'),
      topic: `cantrace/dispositivos/${event.dispositivoId}/telemetria`,
      data: `{ temp: ${event.temperatura}, hum: ${event.umidade}, lum: ${event.luminosidade} }`,
      type: 'success'
    };
    this.mqttEvents = [mqttEvent, ...this.mqttEvents.slice(0, 9)];
  }

  private addTimelineEvent(event: AlertEvent): void {
    const timelineEvent = {
      time: new Date().toLocaleTimeString('pt-BR'),
      type: event.nivel.toLowerCase(),
      title: event.titulo,
      description: event.dispositivoId,
      color: event.nivel === 'CRITICAL' ? '#FF5252' : event.nivel === 'WARN' ? '#FFC107' : '#4FC3F7'
    };
    this.timelineEvents = [timelineEvent, ...this.timelineEvents.slice(0, 9)];
  }
}
