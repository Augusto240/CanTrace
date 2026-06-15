import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardWidgetComponent } from '../../shared/card-widget/card-widget.component';
import { PulseIndicatorComponent } from '../../shared/pulse-indicator/pulse-indicator.component';

@Component({
  selector: 'app-alertas-widget',
  standalone: true,
  imports: [CommonModule, CardWidgetComponent, PulseIndicatorComponent],
  template: `
    <app-card-widget title="Alertas Ativos" icon="⚡">
      <div class="alertas-grid">
        <div class="alerta-stat critical">
          <span class="count">{{ criticos }}</span>
          <span class="label">● Críticos</span>
        </div>
        <div class="alerta-stat warning">
          <span class="count">{{ medios }}</span>
          <span class="label">● Médios</span>
        </div>
        <div class="alerta-stat info">
          <span class="count">{{ baixos }}</span>
          <span class="label">● Baixos</span>
        </div>
      </div>

      <div class="latest-alert" *ngIf="latestAlert">
        <app-pulse-indicator status="offline" [pulse]="true"></app-pulse-indicator>
        <span class="alert-text">{{ latestAlert }}</span>
        <span class="alert-time">{{ latestAlertTime }}</span>
      </div>
    </app-card-widget>
  `,
  styles: [`
    .alertas-grid {
      display: grid;
      grid-template-columns: 1fr 1fr 1fr;
      gap: 12px;
    }
    .alerta-stat {
      text-align: center;
      padding: 12px;
      border-radius: 6px;
    }
    .alerta-stat.critical {
      background: rgba(255,82,82,0.1);
      border: 1px solid rgba(255,82,82,0.3);
    }
    .alerta-stat.warning {
      background: rgba(255,193,7,0.1);
      border: 1px solid rgba(255,193,7,0.3);
    }
    .alerta-stat.info {
      background: rgba(79,195,247,0.1);
      border: 1px solid rgba(79,195,247,0.3);
    }
    .count {
      display: block;
      font-size: 28px;
      font-weight: 600;
    }
    .critical .count { color: #FF5252; }
    .critical .label { color: #FF5252; }
    .warning .count { color: #FFC107; }
    .warning .label { color: #FFC107; }
    .info .count { color: #4FC3F7; }
    .info .label { color: #4FC3F7; }
    .label {
      font-size: 10px;
      margin-top: 4px;
    }
    .latest-alert {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-top: 12px;
      padding: 8px 12px;
      background: rgba(255,82,82,0.05);
      border: 1px solid rgba(255,82,82,0.2);
      border-radius: 4px;
    }
    .alert-text {
      color: #FF5252;
      font-size: 11px;
    }
    .alert-time {
      margin-left: auto;
      color: #8B949E;
      font-size: 10px;
    }
  `]
})
export class AlertasWidgetComponent {
  @Input() criticos = 3;
  @Input() medios = 7;
  @Input() baixos = 12;
  @Input() latestAlert = 'Sala B — Umidade acima do limite (85%)';
  @Input() latestAlertTime = 'há 2min';
}
