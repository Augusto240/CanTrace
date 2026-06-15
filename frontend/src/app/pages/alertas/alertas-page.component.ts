import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertaService, Alerta } from '../../services/alerta.service';
import { CardWidgetComponent } from '../../shared/card-widget/card-widget.component';
import { StatusBadgeComponent } from '../../shared/status-badge/status-badge.component';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-alertas-page',
  standalone: true,
  imports: [CommonModule, CardWidgetComponent, StatusBadgeComponent],
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1>Alertas Ambientais</h1>
      </div>

      <div class="stats-row">
        <div class="stat-card critical">
          <span class="stat-value">{{ countByStatus('ATIVO') }}</span>
          <span class="stat-label">Ativos</span>
        </div>
        <div class="stat-card success">
          <span class="stat-value">{{ countByStatus('RESOLVIDO') }}</span>
          <span class="stat-label">Resolvidos</span>
        </div>
        <div class="stat-card muted">
          <span class="stat-value">{{ countByStatus('IGNORADO') }}</span>
          <span class="stat-label">Ignorados</span>
        </div>
      </div>

      <app-card-widget title="Todos os Alertas" icon="⚡">
        <div class="alerts-list" *ngIf="alertas.length > 0">
          <div class="alert-item" *ngFor="let alerta of alertas" [ngClass]="alerta.nivel.toLowerCase()">
            <div class="alert-header">
              <span class="alert-title">{{ alerta.titulo }}</span>
              <app-status-badge
                [label]="alerta.status"
                [status]="getStatusBadge(alerta.status)"
                [pulse]="alerta.status === 'ATIVO'">
              </app-status-badge>
            </div>
            <div class="alert-desc">{{ alerta.descricao }}</div>
            <div class="alert-meta">
              <span>📅 {{ alerta.criadoEm | date:'dd/MM HH:mm' }}</span>
              <span>📡 {{ alerta.dispositivoCodigo }}</span>
              <span>📊 {{ alerta.valorAtual }} / {{ alerta.valorLimite }}</span>
            </div>
            <div class="alert-actions" *ngIf="alerta.status === 'ATIVO' && isAdmin">
              <button class="btn-resolve" (click)="resolver(alerta.id)">✓ Resolver</button>
              <button class="btn-ignore" (click)="ignorar(alerta.id)">✕ Ignorar</button>
            </div>
          </div>
        </div>
        <div class="empty-state" *ngIf="alertas.length === 0 && !loading">
          <p>Nenhum alerta registrado</p>
        </div>
        <div class="loading-state" *ngIf="loading">
          <p>Carregando alertas...</p>
        </div>
      </app-card-widget>
    </div>
  `,
  styles: [`
    .page-container { padding: 0; }
    .page-header { margin-bottom: 24px; }
    .page-header h1 { color: #E6EDF3; font-size: 24px; margin: 0; }
    .stats-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin-bottom: 24px; }
    .stat-card {
      background: linear-gradient(145deg, #1B2838, #2D3A4A);
      border: 1px solid #3A4A5A; border-radius: 10px; padding: 16px; text-align: center;
    }
    .stat-value { display: block; font-size: 28px; font-weight: 600; }
    .stat-label { font-size: 11px; color: #8B949E; }
    .stat-card.critical .stat-value { color: #FF5252; }
    .stat-card.success .stat-value { color: #00E676; }
    .stat-card.muted .stat-value { color: #8B949E; }
    .alert-item {
      padding: 16px; margin-bottom: 12px; border-radius: 8px;
      background: #0D1B2A; border-left: 4px solid #30363D;
    }
    .alert-item.critical { border-left-color: #FF5252; }
    .alert-item.warn { border-left-color: #FFC107; }
    .alert-item.error { border-left-color: #FF5252; }
    .alert-item.info { border-left-color: #4FC3F7; }
    .alert-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
    .alert-title { color: #E6EDF3; font-weight: 600; }
    .alert-desc { color: #8B949E; font-size: 13px; margin-bottom: 8px; }
    .alert-meta { display: flex; gap: 16px; color: #8B949E; font-size: 11px; }
    .alert-actions { margin-top: 12px; display: flex; gap: 8px; }
    .btn-resolve {
      background: rgba(0,230,118,0.1); border: 1px solid rgba(0,230,118,0.3);
      color: #00E676; padding: 6px 12px; border-radius: 6px; cursor: pointer; font-size: 12px;
    }
    .btn-ignore {
      background: rgba(139,148,158,0.1); border: 1px solid rgba(139,148,158,0.3);
      color: #8B949E; padding: 6px 12px; border-radius: 6px; cursor: pointer; font-size: 12px;
    }
    .empty-state, .loading-state { text-align: center; padding: 40px; color: #8B949E; }
  `]
})
export class AlertasPageComponent implements OnInit {
  alertas: Alerta[] = [];
  loading = true;
  isAdmin = false;

  constructor(
    private alertaService: AlertaService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.hasRole('ADMIN');
    this.carregarAlertas();
  }

  carregarAlertas(): void {
    this.loading = true;
    this.alertaService.listar().subscribe({
      next: (data) => {
        this.alertas = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  countByStatus(status: string): number {
    return this.alertas.filter(a => a.status === status).length;
  }

  getStatusBadge(status: string): 'online' | 'offline' | 'warning' | 'inactive' {
    const map: Record<string, 'online' | 'offline' | 'warning' | 'inactive'> = {
      'ATIVO': 'offline',
      'RESOLVIDO': 'online',
      'IGNORADO': 'inactive'
    };
    return map[status] || 'inactive';
  }

  resolver(id: string): void {
    this.alertaService.resolver(id).subscribe(() => this.carregarAlertas());
  }

  ignorar(id: string): void {
    this.alertaService.ignorar(id).subscribe(() => this.carregarAlertas());
  }
}
