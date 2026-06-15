import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardWidgetComponent } from '../../shared/card-widget/card-widget.component';

interface TimelineEvent {
  time: string;
  type: string;
  title: string;
  description: string;
  color: string;
}

@Component({
  selector: 'app-timeline-eventos-widget',
  standalone: true,
  imports: [CommonModule, CardWidgetComponent],
  template: `
    <app-card-widget title="Timeline de Eventos" icon="📅">
      <div class="timeline">
        <div class="timeline-item" *ngFor="let event of events"
             [style.border-left-color]="event.color">
          <div class="event-time">{{ event.time }}</div>
          <div class="event-title" [style.color]="event.color">{{ event.title }}</div>
          <div class="event-description">{{ event.description }}</div>
        </div>
      </div>
    </app-card-widget>
  `,
  styles: [`
    .timeline {
      display: flex;
      gap: 16px;
      overflow-x: auto;
      padding-bottom: 8px;
    }
    .timeline-item {
      min-width: 180px;
      background: #0D1B2A;
      border-radius: 6px;
      padding: 12px;
      border-left: 3px solid;
    }
    .event-time {
      color: #8B949E;
      font-size: 10px;
    }
    .event-title {
      font-size: 12px;
      font-weight: 600;
      margin-top: 4px;
    }
    .event-description {
      color: #8B949E;
      font-size: 11px;
      margin-top: 4px;
    }
  `]
})
export class TimelineEventosWidgetComponent {
  @Input() events: TimelineEvent[] = [
    { time: '14:32:03', type: 'critical', title: 'Alerta Crítico', description: 'Sala B — Umidade 85%', color: '#FF5252' },
    { time: '14:31:55', type: 'online', title: 'Device Online', description: 'DEV-002 reconectado', color: '#00E676' },
    { time: '14:30:12', type: 'telemetry', title: 'Leitura Recebida', description: 'DEV-001 — 24.5°C', color: '#4FC3F7' },
    { time: '14:28:45', type: 'resolved', title: 'Alerta Resolvido', description: 'Sala A — Temp normalizada', color: '#FFC107' },
    { time: '14:15:00', type: 'maintenance', title: 'Manutenção', description: 'DEV-005 em manutenção', color: '#9CA3AF' },
  ];
}
