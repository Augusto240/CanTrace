import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AuditLog {
  id: string;
  entidade: string;
  entidadeId: string;
  acao: string;
  usuario: string;
  dadosAnteriores?: any;
  dadosAtuais?: any;
  uri?: string;
  metodoHttp?: string;
  criadoEm: string;
}

export interface AuditStats {
  totalRegistros: number;
  porEntidade: { [key: string]: number };
  porAcao: { [key: string]: number };
}

@Injectable({ providedIn: 'root' })
export class AuditService {
  private apiUrl = '/api/v1/auditoria';

  constructor(private http: HttpClient) {}

  listar(entidade?: string, acao?: string, usuario?: string, dataInicio?: string, dataFim?: string, page = 0, size = 20): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (entidade) params = params.set('entidade', entidade);
    if (acao) params = params.set('acao', acao);
    if (usuario) params = params.set('usuario', usuario);
    if (dataInicio) params = params.set('dataInicio', dataInicio);
    if (dataFim) params = params.set('dataFim', dataFim);

    return this.http.get(this.apiUrl, { params });
  }

  buscarPorId(id: string): Observable<AuditLog> {
    return this.http.get<AuditLog>(`${this.apiUrl}/${id}`);
  }

  porEntidade(entidade: string): Observable<AuditLog[]> {
    return this.http.get<AuditLog[]>(`${this.apiUrl}/entidade/${entidade}`);
  }

  estatisticas(): Observable<AuditStats> {
    return this.http.get<AuditStats>(`${this.apiUrl}/estatisticas`);
  }
}
