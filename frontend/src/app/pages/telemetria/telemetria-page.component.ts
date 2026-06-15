import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TelemetriaService, TelemetriaResponse } from '../../services/telemetria.service';
import { DispositivoService, DispositivoResponse } from '../../services/dispositivo.service';
import { CardWidgetComponent } from '../../shared/card-widget/card-widget.component';

@Component({
  selector: 'app-telemetria-page',
  standalone: true,
  imports: [CommonModule, FormsModule, CardWidgetComponent],
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1>Telemetria Ambiental</h1>
      </div>

      <div class="filters">
        <select [(ngModel)]="selectedDeviceId" (change)="carregarLeituras()">
          <option value="">Selecione um dispositivo</option>
          <option *ngFor="let d of dispositivos" [value]="d.id">{{ d.deviceCode }} - {{ d.nome }}</option>
        </select>
      </div>

      <div class="stats-row" *ngIf="leituras.length > 0">
        <div class="stat-card temp">
          <span class="stat-value">{{ mediaTemperatura | number:'1.1-1' }}°C</span>
          <span class="stat-label">Temp. Média</span>
        </div>
        <div class="stat-card hum">
          <span class="stat-value">{{ mediaUmidade | number:'1.1-1' }}%</span>
          <span class="stat-label">Umidade Média</span>
        </div>
        <div class="stat-card lum">
          <span class="stat-value">{{ mediaLuminosidade | number:'1.0-0' }} lux</span>
          <span class="stat-label">Luminosidade Média</span>
        </div>
      </div>

      <app-card-widget title="Leituras Recentes" icon="📈">
        <div class="readings-table" *ngIf="leituras.length > 0">
          <div class="table-header">
            <span class="col-time">Timestamp</span>
            <span class="col-temp">Temperatura</span>
            <span class="col-hum">Umidade</span>
            <span class="col-lum">Luminosidade</span>
            <span class="col-origin">Origem</span>
          </div>
          <div class="table-row" *ngFor="let leitura of leituras">
            <span class="col-time">{{ leitura.timestamp | date:'dd/MM HH:mm:ss' }}</span>
            <span class="col-temp">{{ leitura.temperatura | number:'1.1-1' }}°C</span>
            <span class="col-hum">{{ leitura.umidade | number:'1.1-1' }}%</span>
            <span class="col-lum">{{ leitura.luminosidade | number:'1.0-0' }} lux</span>
            <span class="col-origin">{{ leitura.origem }}</span>
          </div>
        </div>
        <div class="empty-state" *ngIf="leituras.length === 0 && !loading">
          <p>Selecione um dispositivo para ver as leituras</p>
        </div>
        <div class="loading-state" *ngIf="loading">
          <p>Carregando leituras...</p>
        </div>
      </app-card-widget>
    </div>
  `,
  styles: [`
    .page-container { padding: 0; }
    .page-header { margin-bottom: 24px; }
    .page-header h1 { color: #E6EDF3; font-size: 24px; margin: 0; }
    .filters { margin-bottom: 24px; }
    .filters select {
      background: #0D1B2A; border: 1px solid #30363D; border-radius: 8px;
      padding: 10px 16px; color: #E6EDF3; font-size: 14px; min-width: 300px;
    }
    .stats-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin-bottom: 24px; }
    .stat-card {
      background: linear-gradient(145deg, #1B2838, #2D3A4A);
      border: 1px solid #3A4A5A; border-radius: 10px; padding: 16px; text-align: center;
    }
    .stat-value { display: block; font-size: 24px; font-weight: 600; }
    .stat-label { font-size: 11px; color: #8B949E; }
    .stat-card.temp .stat-value { color: #FF5252; }
    .stat-card.hum .stat-value { color: #4FC3F7; }
    .stat-card.lum .stat-value { color: #FFC107; }
    .readings-table { font-size: 13px; }
    .table-header, .table-row { display: grid; grid-template-columns: 1.5fr 1fr 1fr 1fr 1fr; padding: 12px 8px; }
    .table-header { color: #8B949E; font-size: 11px; text-transform: uppercase; border-bottom: 1px solid #30363D; }
    .table-row { border-bottom: 1px solid #1B2838; color: #E6EDF3; }
    .empty-state, .loading-state { text-align: center; padding: 40px; color: #8B949E; }
  `]
})
export class TelemetriaPageComponent implements OnInit {
  dispositivos: DispositivoResponse[] = [];
  leituras: TelemetriaResponse[] = [];
  selectedDeviceId = '';
  loading = false;

  mediaTemperatura = 0;
  mediaUmidade = 0;
  mediaLuminosidade = 0;

  constructor(
    private telemetriaService: TelemetriaService,
    private dispositivoService: DispositivoService
  ) {}

  ngOnInit(): void {
    this.dispositivoService.listar().subscribe(data => {
      this.dispositivos = data;
    });
  }

  carregarLeituras(): void {
    if (!this.selectedDeviceId) return;
    this.loading = true;
    this.telemetriaService.listar(this.selectedDeviceId).subscribe({
      next: (data) => {
        this.leituras = data;
        this.calcularMedias();
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  calcularMedias(): void {
    if (this.leituras.length === 0) return;
    this.mediaTemperatura = this.leituras.reduce((sum, l) => sum + l.temperatura, 0) / this.leituras.length;
    this.mediaUmidade = this.leituras.reduce((sum, l) => sum + l.umidade, 0) / this.leituras.length;
    this.mediaLuminosidade = this.leituras.reduce((sum, l) => sum + l.luminosidade, 0) / this.leituras.length;
  }
}
