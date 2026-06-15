import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardWidgetComponent } from '../../shared/card-widget/card-widget.component';

interface DevicePin {
  id: string;
  x: number;
  y: number;
  status: 'online' | 'offline' | 'warning';
}

@Component({
  selector: 'app-mapa-operacional-widget',
  standalone: true,
  imports: [CommonModule, CardWidgetComponent],
  template: `
    <app-card-widget title="Mapa Operacional" icon="🗺️">
      <div class="legend">
        <span class="legend-item"><span class="dot online"></span> Online</span>
        <span class="legend-item"><span class="dot offline"></span> Offline</span>
      </div>

      <div class="map-container">
        <div class="grid-overlay"></div>
        <div class="device-pin" *ngFor="let device of devices"
             [style.left.%]="device.x"
             [style.top.%]="device.y"
             [ngClass]="device.status">
          <div class="pin-dot"></div>
          <div class="pin-label">{{ device.id }}</div>
        </div>
      </div>
    </app-card-widget>
  `,
  styles: [`
    .legend {
      display: flex;
      gap: 12px;
      margin-bottom: 12px;
    }
    .legend-item {
      display: flex;
      align-items: center;
      gap: 4px;
      color: #8B949E;
      font-size: 10px;
    }
    .dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
    }
    .dot.online { background: #00E676; }
    .dot.offline { background: #FF5252; }
    .map-container {
      position: relative;
      background: #0D1B2A;
      border-radius: 6px;
      height: 200px;
      overflow: hidden;
    }
    .grid-overlay {
      position: absolute;
      inset: 0;
      background-image: linear-gradient(rgba(30,40,55,0.5) 1px, transparent 1px),
                        linear-gradient(90deg, rgba(30,40,55,0.5) 1px, transparent 1px);
      background-size: 40px 40px;
    }
    .device-pin {
      position: absolute;
      text-align: center;
      transform: translate(-50%, -50%);
    }
    .pin-dot {
      width: 12px;
      height: 12px;
      border-radius: 50%;
      border: 2px solid #0A1628;
      margin: 0 auto;
    }
    .online .pin-dot {
      background: #00E676;
      box-shadow: 0 0 8px #00E676;
    }
    .offline .pin-dot {
      background: #FF5252;
      box-shadow: 0 0 8px #FF5252;
    }
    .warning .pin-dot {
      background: #FFC107;
      box-shadow: 0 0 8px #FFC107;
    }
    .pin-label {
      color: #8B949E;
      font-size: 8px;
      margin-top: 2px;
    }
  `]
})
export class MapaOperacionalWidgetComponent {
  @Input() devices: DevicePin[] = [
    { id: 'DEV-001', x: 20, y: 30, status: 'online' },
    { id: 'DEV-002', x: 60, y: 50, status: 'online' },
    { id: 'DEV-003', x: 35, y: 70, status: 'offline' },
    { id: 'DEV-004', x: 75, y: 25, status: 'warning' },
  ];
}
