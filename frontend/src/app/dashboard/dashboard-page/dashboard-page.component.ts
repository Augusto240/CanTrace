import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { DashboardService, DashboardData } from '../../services/dashboard.service';
import { WebSocketService } from '../../services/websocket.service';
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
    <div class="dashboard-grid">
      <div class="row-1">
        <app-alertas-widget></app-alertas-widget>
        <app-saude-sistema-widget></app-saude-sistema-widget>
      </div>

      <div class="row-2">
        <app-eventos-mqtt-widget></app-eventos-mqtt-widget>
        <app-mapa-operacional-widget></app-mapa-operacional-widget>
      </div>

      <div class="row-3">
        <app-timeline-eventos-widget></app-timeline-eventos-widget>
      </div>
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
  `]
})
export class DashboardPageComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  constructor(
    private dashboardService: DashboardService,
    private websocketService: WebSocketService
  ) {}

  ngOnInit(): void {
    this.dashboardService.getDashboard()
      .pipe(takeUntil(this.destroy$))
      .subscribe();

    this.websocketService.connect();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.websocketService.disconnect();
  }
}
