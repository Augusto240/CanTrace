import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-card-widget',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="card">
      <div class="card-header" *ngIf="title">
        <div class="card-icon" *ngIf="icon">{{ icon }}</div>
        <span class="card-title">{{ title }}</span>
        <ng-content select="[header-action]"></ng-content>
      </div>
      <div class="card-body">
        <ng-content></ng-content>
      </div>
    </div>
  `,
  styles: [`
    .card {
      background: linear-gradient(145deg, #1B2838, #2D3A4A);
      border: 1px solid #3A4A5A;
      border-radius: 10px;
      padding: 16px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.3);
      transition: box-shadow 0.2s ease;
    }
    .card:hover {
      box-shadow: 0 6px 16px rgba(0,0,0,0.4);
    }
    .card-header {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 12px;
    }
    .card-icon {
      font-size: 16px;
    }
    .card-title {
      color: #8B949E;
      font-size: 11px;
      text-transform: uppercase;
      letter-spacing: 1px;
    }
    .card-header ::ng-deep {
      margin-left: auto;
    }
  `]
})
export class CardWidgetComponent {
  @Input() title = '';
  @Input() icon = '';
}
