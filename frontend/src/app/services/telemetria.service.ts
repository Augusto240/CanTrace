import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TelemetriaRequest {
  dispositivoId: string;
  temperatura: number;
  umidade: number;
  luminosidade: number;
  origem: 'MANUAL' | 'MQTT' | 'IMPORTACAO';
  timestamp?: string;
}

export interface TelemetriaResponse {
  id: string;
  dispositivoId: string;
  dispositivoCodigo: string;
  temperatura: number;
  umidade: number;
  luminosidade: number;
  origem: string;
  timestamp: string;
  criadoEm: string;
}

@Injectable({ providedIn: 'root' })
export class TelemetriaService {
  private apiUrl = '/api/v1/telemetria';

  constructor(private http: HttpClient) {}

  listar(dispositivoId: string, inicio?: string, fim?: string, page = 0, size = 20): Observable<TelemetriaResponse[]> {
    let params = new HttpParams()
      .set('dispositivoId', dispositivoId)
      .set('page', page.toString())
      .set('size', size.toString());

    if (inicio) params = params.set('inicio', inicio);
    if (fim) params = params.set('fim', fim);

    return this.http.get<TelemetriaResponse[]>(this.apiUrl, { params });
  }

  buscarPorId(id: string): Observable<TelemetriaResponse> {
    return this.http.get<TelemetriaResponse>(`${this.apiUrl}/${id}`);
  }

  registrar(telemetria: TelemetriaRequest): Observable<TelemetriaResponse> {
    return this.http.post<TelemetriaResponse>(this.apiUrl, telemetria);
  }
}
