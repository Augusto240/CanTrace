import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardWidgetComponent } from '../../shared/card-widget/card-widget.component';
import { PulseIndicatorComponent } from '../../shared/pulse-indicator/pulse-indicator.component';

@Component({
  selector: 'app-saude-sistema-widget',
  standalone: true,
  imports: [CommonModule, CardWidgetComponent, PulseIndicatorComponent],
  template: `
    <app-card-widget title="Saúde do Sistema" icon="💚">
      <div class="status-header">
        <app-pulse-indicator status="online" [pulse]="true"></app-pulse-indicator>
        <span>Todos operacionais</span>
      </div>

      <div class="services-grid">
        <div class="service-item" *ngFor="let service of services">
          <div class="service-header">
            <app-pulse-indicator [status]="service.status"></app-pulse-indicator>
            <span class="service-name">{{ service.name }}</span>
          </div>
          <div class="service-value">{{ service.value }}</div>
          <div class="service-detail">{{ service.detail }}</div>
        </div>
      </div>
    </app-card-widget>
  `,
  styles: [`
    .status-header {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 12px;
      color: #00E676;
      font-size: 11px;
    }
    .services-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 12px;
    }
    .service-item {
      background: #0D1B2A;
      border-radius: 6px;
      padding: 12px;
    }
    .service-header {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 8px;
    }
    .service-name {
      color: #8B949E;
      font-size: 11px;
    }
    .service-value {
      color: #E6EDF3;
      font-size: 13px;
      font-weight: 600;
    }
    .service-detail {
      color: #8B949E;
      font-size: 10px;
    }
  `]
})
export class SaudeSistemaWidgetComponent {
  services = [
    { name: 'MQTT Broker', value: 'Conectado', detail: 'Latência: 12ms', status: 'online' as const },
    { name: 'PostgreSQL', value: 'Online', detail: 'Queries: 2.3ms', status: 'online' as const },
    { name: 'API REST', value: 'Respondendo', detail: 'Uptime: 99.9%', status: 'online' as const },
    { name: 'WebSocket', value: 'Ativo', detail: 'Clientes: 3', status: 'online' as const },
  ];
}
