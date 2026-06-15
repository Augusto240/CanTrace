import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DispositivoService, DispositivoResponse } from '../../services/dispositivo.service';
import { StatusBadgeComponent } from '../../shared/status-badge/status-badge.component';
import { CardWidgetComponent } from '../../shared/card-widget/card-widget.component';

@Component({
  selector: 'app-dispositivos-page',
  standalone: true,
  imports: [CommonModule, RouterModule, StatusBadgeComponent, CardWidgetComponent],
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1>Dispositivos IoT</h1>
        <button class="btn-primary" (click)="showCreateModal = true">
          + Novo Dispositivo
        </button>
      </div>

      <div class="stats-row">
        <div class="stat-card">
          <span class="stat-value">{{ dispositivos.length }}</span>
          <span class="stat-label">Total</span>
        </div>
        <div class="stat-card active">
          <span class="stat-value">{{ countByStatus('ATIVO') }}</span>
          <span class="stat-label">Ativos</span>
        </div>
        <div class="stat-card warning">
          <span class="stat-value">{{ countByStatus('MANUTENCAO') }}</span>
          <span class="stat-label">Manutenção</span>
        </div>
        <div class="stat-card danger">
          <span class="stat-value">{{ countByStatus('OFFLINE') }}</span>
          <span class="stat-label">Offline</span>
        </div>
      </div>

      <app-card-widget title="Todos os Dispositivos" icon="📡">
        <div class="devices-table" *ngIf="dispositivos.length > 0">
          <div class="table-header">
            <span class="col-code">Código</span>
            <span class="col-name">Nome</span>
            <span class="col-area">Área</span>
            <span class="col-status">Status</span>
            <span class="col-actions">Ações</span>
          </div>
          <div class="table-row" *ngFor="let device of dispositivos">
            <span class="col-code">{{ device.deviceCode }}</span>
            <span class="col-name">{{ device.nome }}</span>
            <span class="col-area">{{ device.area }}</span>
            <span class="col-status">
              <app-status-badge
                [label]="device.status"
                [status]="getStatusBadge(device.status)"
                [pulse]="device.status === 'ATIVO'">
              </app-status-badge>
            </span>
            <span class="col-actions">
              <button class="btn-icon" title="Editar">✏️</button>
              <button class="btn-icon danger" title="Deletar" (click)="deletar(device.id)">🗑️</button>
            </span>
          </div>
        </div>
        <div class="empty-state" *ngIf="dispositivos.length === 0 && !loading">
          <p>Nenhum dispositivo cadastrado</p>
        </div>
        <div class="loading-state" *ngIf="loading">
          <p>Carregando dispositivos...</p>
        </div>
      </app-card-widget>
    </div>
  `,
  styles: [`
    .page-container { padding: 0; }
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
    .page-header h1 { color: #E6EDF3; font-size: 24px; margin: 0; }
    .btn-primary {
      background: linear-gradient(135deg, #00E676, #4A7C59);
      border: none; border-radius: 8px; padding: 10px 20px;
      color: #0A1628; font-weight: 600; cursor: pointer;
    }
    .stats-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 24px; }
    .stat-card {
      background: linear-gradient(145deg, #1B2838, #2D3A4A);
      border: 1px solid #3A4A5A; border-radius: 10px; padding: 16px; text-align: center;
    }
    .stat-value { display: block; font-size: 28px; font-weight: 600; color: #E6EDF3; }
    .stat-label { font-size: 11px; color: #8B949E; }
    .stat-card.active .stat-value { color: #00E676; }
    .stat-card.warning .stat-value { color: #FFC107; }
    .stat-card.danger .stat-value { color: #FF5252; }
    .devices-table { font-size: 13px; }
    .table-header, .table-row { display: grid; grid-template-columns: 1fr 1.5fr 1fr 1fr 100px; padding: 12px 8px; }
    .table-header { color: #8B949E; font-size: 11px; text-transform: uppercase; border-bottom: 1px solid #30363D; }
    .table-row { border-bottom: 1px solid #1B2838; color: #E6EDF3; }
    .col-actions { display: flex; gap: 8px; }
    .btn-icon { background: transparent; border: none; cursor: pointer; font-size: 14px; padding: 4px; }
    .btn-icon.danger:hover { opacity: 0.7; }
    .empty-state, .loading-state { text-align: center; padding: 40px; color: #8B949E; }
  `]
})
export class DispositivosPageComponent implements OnInit {
  dispositivos: DispositivoResponse[] = [];
  loading = true;
  showCreateModal = false;

  constructor(private dispositivoService: DispositivoService) {}

  ngOnInit(): void {
    this.carregarDispositivos();
  }

  carregarDispositivos(): void {
    this.loading = true;
    this.dispositivoService.listar().subscribe({
      next: (data) => {
        this.dispositivos = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  countByStatus(status: string): number {
    return this.dispositivos.filter(d => d.status === status).length;
  }

  getStatusBadge(status: string): 'online' | 'offline' | 'warning' | 'inactive' {
    const map: Record<string, 'online' | 'offline' | 'warning' | 'inactive'> = {
      'ATIVO': 'online',
      'OFFLINE': 'offline',
      'MANUTENCAO': 'warning',
      'INATIVO': 'inactive'
    };
    return map[status] || 'inactive';
  }

  deletar(id: string): void {
    if (confirm('Tem certeza que deseja deletar este dispositivo?')) {
      this.dispositivoService.deletar(id).subscribe(() => {
        this.carregarDispositivos();
      });
    }
  }
}
