import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, tap, catchError, map } from 'rxjs';

export interface User {
  username: string;
  role: 'ADMIN' | 'OPERATOR';
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly USER_KEY = 'cantrace_user';
  private readonly CREDS_KEY = 'cantrace_creds';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<boolean> {
    const credentials = btoa(`${username}:${password}`);
    const headers = { 'Authorization': `Basic ${credentials}` };

    return this.http.get('/api/db/ping', { headers, responseType: 'text' }).pipe(
      tap(() => {
        const role = username === 'admin' ? 'ADMIN' : 'OPERATOR';
        const user: User = { username, role };
        sessionStorage.setItem(this.USER_KEY, JSON.stringify(user));
        sessionStorage.setItem(this.CREDS_KEY, credentials);
      }),
      map(() => true),
      catchError(() => of(false))
    );
  }

  logout(): void {
    sessionStorage.removeItem(this.USER_KEY);
    sessionStorage.removeItem(this.CREDS_KEY);
  }

  isAuthenticated(): boolean {
    return !!sessionStorage.getItem(this.CREDS_KEY);
  }

  getUser(): User | null {
    const data = sessionStorage.getItem(this.USER_KEY);
    return data ? JSON.parse(data) : null;
  }

  getToken(): string | null {
    return sessionStorage.getItem(this.CREDS_KEY);
  }

  hasRole(role: 'ADMIN' | 'OPERATOR'): boolean {
    const user = this.getUser();
    return user?.role === role;
  }
}
