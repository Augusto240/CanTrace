import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Alerta {
  id: string;
  titulo: string;
  descricao: string;
  nivel: 'INFO' | 'WARN' | 'ERROR' | 'CRITICAL';
  status: 'ATIVO' | 'RESOLVIDO' | 'IGNORADO';
  dispositivoId: string;
  dispositivoCodigo: string;
  regra: string;
  valorAtual: number;
  valorLimite: number;
  usuario?: string;
  criadoEm: string;
  resolvidoEm?: string;
}

@Injectable({ providedIn: 'root' })
export class AlertaService {
  private apiUrl = '/api/v1/alertas';

  constructor(private http: HttpClient) {}

  listar(status?: string, nivel?: string, dispositivoId?: string, page = 0, size = 20): Observable<Alerta[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (status) params = params.set('status', status);
    if (nivel) params = params.set('nivel', nivel);
    if (dispositivoId) params = params.set('dispositivoId', dispositivoId);

    return this.http.get<Alerta[]>(this.apiUrl, { params });
  }

  buscarPorId(id: string): Observable<Alerta> {
    return this.http.get<Alerta>(`${this.apiUrl}/${id}`);
  }

  resolver(id: string, usuario = 'sistema'): Observable<Alerta> {
    return this.http.put<Alerta>(`${this.apiUrl}/${id}/resolver`, { usuario });
  }

  ignorar(id: string, usuario = 'sistema'): Observable<Alerta> {
    return this.http.put<Alerta>(`${this.apiUrl}/${id}/ignorar`, { usuario });
  }
}
