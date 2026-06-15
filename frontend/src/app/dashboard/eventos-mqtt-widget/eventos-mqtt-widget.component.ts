import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardWidgetComponent } from '../../shared/card-widget/card-widget.component';

@Component({
  selector: 'app-eventos-mqtt-widget',
  standalone: true,
  imports: [CommonModule, CardWidgetComponent],
  template: `
    <app-card-widget title="Eventos MQTT" icon="📡">
      <div class="events-feed">
        <div class="event-item" *ngFor="let event of events" [ngClass]="event.type">
          <span class="event-time">{{ event.time }}</span>
          <span class="event-arrow">→</span>
          <span class="event-topic">{{ event.topic }}</span>
          <div class="event-data">{{ event.data }}</div>
        </div>
      </div>
    </app-card-widget>
  `,
  styles: [`
    .events-feed {
      font-family: monospace;
      font-size: 10px;
      line-height: 1.8;
    }
    .event-item {
      margin-bottom: 4px;
    }
    .event-time {
      color: #00E676;
    }
    .event-arrow {
      color: #8B949E;
      margin: 0 4px;
    }
    .event-topic {
      color: #8B949E;
    }
    .event-data {
      color: #8B949E;
      padding-left: 24px;
      font-size: 9px;
    }
    .event-item.success .event-time { color: #00E676; }
    .event-item.error .event-time { color: #FF5252; }
    .event-item.warning .event-time { color: #FFC107; }
  `]
})
export class EventosMqttWidgetComponent {
  @Input() events = [
    { time: '14:32:07', topic: 'cantrace/area-01/temp', data: '{ temp: 24.5, hum: 65 }', type: 'success' },
    { time: '14:32:05', topic: 'cantrace/area-02/temp', data: '{ temp: 22.1, hum: 70 }', type: 'success' },
    { time: '14:32:03', topic: 'cantrace/area-01/alert', data: '{ type: "humidity_high" }', type: 'error' },
    { time: '14:31:58', topic: 'cantrace/area-03/temp', data: '{ temp: 23.8, hum: 62 }', type: 'success' },
    { time: '14:31:55', topic: 'cantrace/area-02/status', data: '{ status: "online" }', type: 'warning' },
  ];
}
