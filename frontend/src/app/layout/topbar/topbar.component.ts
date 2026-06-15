import { Component, Input } from '@angular/core';
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
  @Input() mqttConnected = true;
  @Input() lastUpdate = new Date();

  onRefresh(): void {
    this.lastUpdate = new Date();
  }
}
