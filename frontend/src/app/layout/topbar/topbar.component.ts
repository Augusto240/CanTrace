import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss']
})
export class TopbarComponent {
  @Input() title = 'Dashboard Operacional';
  @Input() mqttStatus: 'connected' | 'disconnected' | 'reconnecting' = 'disconnected';
  @Input() lastUpdate = new Date();
  @Output() refresh = new EventEmitter<void>();

  onRefresh(): void {
    this.lastUpdate = new Date();
    this.refresh.emit();
  }
}
