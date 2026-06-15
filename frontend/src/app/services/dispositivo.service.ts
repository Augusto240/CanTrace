import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DispositivoRequest {
  deviceCode: string;
  nome: string;
  area: string;
  tipoSensors: string[];
}

export interface DispositivoResponse {
  id: string;
  deviceCode: string;
  nome: string;
  area: string;
  status: 'ATIVO' | 'INATIVO' | 'MANUTENCAO' | 'OFFLINE';
  tipoSensors: string[];
  criadoEm: string;
  atualizadoEm: string;
}

export interface DispositivoStatusRequest {
  status: 'ATIVO' | 'INATIVO' | 'MANUTENCAO' | 'OFFLINE';
}

@Injectable({ providedIn: 'root' })
export class DispositivoService {
  private apiUrl = '/api/v1/dispositivos';

  constructor(private http: HttpClient) {}

  listar(): Observable<DispositivoResponse[]> {
    return this.http.get<DispositivoResponse[]>(this.apiUrl);
  }

  buscarPorId(id: string): Observable<DispositivoResponse> {
    return this.http.get<DispositivoResponse>(`${this.apiUrl}/${id}`);
  }

  criar(dispositivo: DispositivoRequest): Observable<DispositivoResponse> {
    return this.http.post<DispositivoResponse>(this.apiUrl, dispositivo);
  }

  atualizar(id: string, dispositivo: DispositivoRequest): Observable<DispositivoResponse> {
    return this.http.put<DispositivoResponse>(`${this.apiUrl}/${id}`, dispositivo);
  }

  mudarStatus(id: string, status: DispositivoStatusRequest): Observable<DispositivoResponse> {
    return this.http.patch<DispositivoResponse>(`${this.apiUrl}/${id}/status`, status);
  }

  deletar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  ultimaLeitura(id: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}/ultima-leitura`);
  }

  leituras(id: string, page = 0, size = 20): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${id}/leituras`, {
      params: { page: page.toString(), size: size.toString() }
    });
  }
}
