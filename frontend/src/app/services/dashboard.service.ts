import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

export interface DashboardData {
  totalDispositivosAtivo: number;
  totalDispositivosInativo: number;
  totalDispositivosManutencao: number;
  totalDispositivosOffline: number;
  totalLeituras24h: number;
  totalLeituras7d: number;
  mediaTemperatura24h: number | null;
  mediaUmidade24h: number | null;
  mediaLuminosidade24h: number | null;
  totalAlertasAtivo: number;
  totalAlertasResolvido: number;
  totalAlertasIgnorado: number;
  totalRegistrosAuditoria: number;
  ultimasLeituras: any[];
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private apiUrl = '/api/v1/dashboard';
  private dashboardSubject = new BehaviorSubject<DashboardData | null>(null);
  
  dashboard$ = this.dashboardSubject.asObservable();

  constructor(private http: HttpClient) {}

  getDashboard(): Observable<DashboardData> {
    return this.http.get<DashboardData>(this.apiUrl).pipe(
      tap(data => this.dashboardSubject.next(data))
    );
  }
}
