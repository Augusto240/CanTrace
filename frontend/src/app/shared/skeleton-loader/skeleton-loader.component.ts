import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-skeleton-loader',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="skeleton" [ngClass]="type">
      <div class="skeleton-line" *ngFor="let line of lines; let i = index"
           [style.width]="line.width"
           [style.animation-delay]="i * 0.1 + 's'">
      </div>
    </div>
  `,
  styles: [`
    .skeleton {
      display: flex;
      flex-direction: column;
      gap: 8px;
    }
    .skeleton-line {
      height: 12px;
      background: #0D1B2A;
      border-radius: 4px;
      animation: shimmer 1.5s infinite;
    }
    .card .skeleton-line:first-child { width: 60%; }
    .card .skeleton-line:last-child { width: 40%; }
    .row .skeleton-line { display: inline-block; margin-right: 8px; }
    .row .skeleton-line:first-child { width: 30%; }
    .row .skeleton-line:last-child { width: 60%; }
    @keyframes shimmer { 0% { opacity: 0.3; } 50% { opacity: 0.6; } 100% { opacity: 0.3; } }
  `]
})
export class SkeletonLoaderComponent {
  @Input() type: 'card' | 'row' = 'card';

  get lines(): { width: string }[] {
    return this.type === 'card'
      ? [{ width: '60%' }, { width: '40%' }]
      : [{ width: '30%' }, { width: '60%' }];
  }
}
