import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuditService, AuditLog, AuditStats } from '../../services/audit.service';
import { CardWidgetComponent } from '../../shared/card-widget/card-widget.component';

@Component({
  selector: 'app-auditoria-page',
  standalone: true,
  imports: [CommonModule, CardWidgetComponent],
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1>Auditoria</h1>
      </div>

      <div class="stats-row" *ngIf="stats">
        <div class="stat-card">
          <span class="stat-value">{{ stats.totalRegistros }}</span>
          <span class="stat-label">Total Registros</span>
        </div>
        <div class="stat-card">
          <span class="stat-value">{{ stats.porEntidade['DispositivoIoT'] || 0 }}</span>
          <span class="stat-label">Dispositivos</span>
        </div>
        <div class="stat-card">
          <span class="stat-value">{{ stats.porEntidade['LeituraAmbiental'] || 0 }}</span>
          <span class="stat-label">Leituras</span>
        </div>
        <div class="stat-card">
          <span class="stat-value">{{ stats.porEntidade['AlertaAmbiental'] || 0 }}</span>
          <span class="stat-label">Alertas</span>
        </div>
      </div>

      <app-card-widget title="Logs de Auditoria" icon="📋">
        <div class="audit-list" *ngIf="logs.length > 0">
          <div class="audit-item" *ngFor="let log of logs">
            <div class="audit-header">
              <span class="audit-entity">{{ log.entidade }}</span>
              <span class="audit-action" [ngClass]="log.acao.toLowerCase()">{{ log.acao }}</span>
            </div>
            <div class="audit-details">
              <span>👤 {{ log.usuario }}</span>
              <span>📅 {{ log.criadoEm | date:'dd/MM HH:mm:ss' }}</span>
              <span *ngIf="log.metodoHttp">🔗 {{ log.metodoHttp }} {{ log.uri }}</span>
            </div>
          </div>
        </div>
        <div class="empty-state" *ngIf="logs.length === 0 && !loading">
          <p>Nenhum registro de auditoria</p>
        </div>
        <div class="loading-state" *ngIf="loading">
          <p>Carregando auditoria...</p>
        </div>
      </app-card-widget>
    </div>
  `,
  styles: [`
    .page-container { padding: 0; }
    .page-header { margin-bottom: 24px; }
    .page-header h1 { color: #E6EDF3; font-size: 24px; margin: 0; }
    .stats-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 24px; }
    .stat-card {
      background: linear-gradient(145deg, #1B2838, #2D3A4A);
      border: 1px solid #3A4A5A; border-radius: 10px; padding: 16px; text-align: center;
    }
    .stat-value { display: block; font-size: 24px; font-weight: 600; color: #E6EDF3; }
    .stat-label { font-size: 11px; color: #8B949E; }
    .audit-item {
      padding: 12px; margin-bottom: 8px; border-radius: 6px;
      background: #0D1B2A; border-left: 3px solid #30363D;
    }
    .audit-header { display: flex; justify-content: space-between; margin-bottom: 6px; }
    .audit-entity { color: #E6EDF3; font-weight: 500; }
    .audit-action {
      font-size: 11px; padding: 2px 8px; border-radius: 4px;
    }
    .audit-action.create { background: rgba(0,230,118,0.15); color: #00E676; }
    .audit-action.update { background: rgba(79,195,247,0.15); color: #4FC3F7; }
    .audit-action.delete { background: rgba(255,82,82,0.15); color: #FF5252; }
    .audit-details { display: flex; gap: 16px; color: #8B949E; font-size: 11px; }
    .empty-state, .loading-state { text-align: center; padding: 40px; color: #8B949E; }
  `]
})
export class AuditoriaPageComponent implements OnInit {
  logs: AuditLog[] = [];
  stats: AuditStats | null = null;
  loading = true;

  constructor(private auditService: AuditService) {}

  ngOnInit(): void {
    this.carregarDados();
  }

  carregarDados(): void {
    this.loading = true;
    this.auditService.listar().subscribe({
      next: (data: any) => {
        this.logs = data.content || data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });

    this.auditService.estatisticas().subscribe(data => {
      this.stats = data;
    });
  }
}
