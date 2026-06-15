import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-status-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span class="badge" [ngClass]="statusClass">
      <span class="dot" *ngIf="pulse"></span>
      {{ label }}
    </span>
  `,
  styles: [`
    .badge {
      display: inline-flex;
      align-items: center;
      gap: 6px;
      padding: 4px 12px;
      border-radius: 20px;
      font-size: 11px;
      font-weight: 500;
    }
    .dot {
      width: 6px;
      height: 6px;
      border-radius: 50%;
    }
    .online { background: rgba(0,230,118,0.15); color: #00E676; border: 1px solid rgba(0,230,118,0.3); }
    .online .dot { background: #00E676; animation: pulse 2s infinite; }
    .offline { background: rgba(255,82,82,0.15); color: #FF5252; border: 1px solid rgba(255,82,82,0.3); }
    .offline .dot { background: #FF5252; animation: pulse 1s infinite; }
    .warning { background: rgba(255,193,7,0.15); color: #FFC107; border: 1px solid rgba(255,193,7,0.3); }
    .warning .dot { background: #FFC107; }
    .inactive { background: rgba(156,163,175,0.15); color: #9CA3AF; border: 1px solid rgba(156,163,175,0.3); }
    @keyframes pulse { 0%,100% { opacity: 1; } 50% { opacity: 0.5; } }
  `]
})
export class StatusBadgeComponent {
  @Input() label = '';
  @Input() status: 'online' | 'offline' | 'warning' | 'inactive' = 'offline';
  @Input() pulse = false;

  get statusClass(): string {
    return this.status;
  }
}
