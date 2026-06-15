import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pulse-indicator',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span class="indicator" [ngClass]="status" [class.pulse]="pulse"></span>
  `,
  styles: [`
    .indicator {
      display: inline-block;
      width: 10px;
      height: 10px;
      border-radius: 50%;
    }
    .indicator.pulse { animation: pulse 2s infinite; }
    .online { background: #00E676; }
    .offline { background: #FF5252; animation-duration: 1s !important; }
    .warning { background: #FFC107; animation-duration: 1.5s !important; }
    .inactive { background: #9CA3AF; }
    @keyframes pulse { 0%,100% { opacity: 1; transform: scale(1); } 50% { opacity: 0.5; transform: scale(1.1); } }
  `]
})
export class PulseIndicatorComponent {
  @Input() status: 'online' | 'offline' | 'warning' | 'inactive' = 'offline';
  @Input() pulse = true;
}
