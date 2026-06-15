import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <div class="logo-icon">🌿</div>
          <h1 class="logo-title">CANTRACE</h1>
          <p class="logo-subtitle">Environmental Operations Center</p>
        </div>

        <form (ngSubmit)="onSubmit()" class="login-form">
          <div class="form-group">
            <label for="username">Usuário</label>
            <input
              id="username"
              type="text"
              [(ngModel)]="username"
              name="username"
              placeholder="admin"
              autocomplete="username"
              required
            />
          </div>

          <div class="form-group">
            <label for="password">Senha</label>
            <input
              id="password"
              type="password"
              [(ngModel)]="password"
              name="password"
              placeholder="••••••••"
              autocomplete="current-password"
              required
            />
          </div>

          <div class="error-message" *ngIf="errorMessage">
            {{ errorMessage }}
          </div>

          <button type="submit" [disabled]="loading" class="login-btn">
            <span *ngIf="!loading">Entrar</span>
            <span *ngIf="loading">Conectando...</span>
          </button>
        </form>

        <div class="login-footer">
          <p>Credenciais: admin/admin123 ou operator/operator123</p>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #0A1628 0%, #0D1B2A 50%, #1A2A3A 100%);
    }
    .login-card {
      width: 100%;
      max-width: 400px;
      background: linear-gradient(145deg, #1B2838, #2D3A4A);
      border: 1px solid #3A4A5A;
      border-radius: 16px;
      padding: 40px;
      box-shadow: 0 8px 32px rgba(0,0,0,0.4);
    }
    .login-header {
      text-align: center;
      margin-bottom: 32px;
    }
    .logo-icon {
      width: 48px;
      height: 48px;
      background: linear-gradient(135deg, #00E676, #4A7C59);
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 24px;
      margin: 0 auto 16px;
    }
    .logo-title {
      color: #E6EDF3;
      font-size: 24px;
      font-weight: 700;
      margin: 0;
    }
    .logo-subtitle {
      color: #4A7C59;
      font-size: 12px;
      margin: 4px 0 0;
    }
    .form-group {
      margin-bottom: 16px;
    }
    .form-group label {
      display: block;
      color: #8B949E;
      font-size: 12px;
      margin-bottom: 6px;
    }
    .form-group input {
      width: 100%;
      padding: 12px 16px;
      background: #0D1B2A;
      border: 1px solid #30363D;
      border-radius: 8px;
      color: #E6EDF3;
      font-size: 14px;
      outline: none;
      transition: border-color 0.2s;
    }
    .form-group input:focus {
      border-color: #00E676;
    }
    .form-group input::placeholder {
      color: #4A5568;
    }
    .error-message {
      color: #FF5252;
      font-size: 12px;
      margin-bottom: 16px;
      padding: 8px 12px;
      background: rgba(255,82,82,0.1);
      border: 1px solid rgba(255,82,82,0.3);
      border-radius: 6px;
    }
    .login-btn {
      width: 100%;
      padding: 12px;
      background: linear-gradient(135deg, #00E676, #4A7C59);
      border: none;
      border-radius: 8px;
      color: #0A1628;
      font-size: 14px;
      font-weight: 600;
      cursor: pointer;
      transition: opacity 0.2s;
    }
    .login-btn:hover {
      opacity: 0.9;
    }
    .login-btn:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }
    .login-footer {
      margin-top: 24px;
      text-align: center;
    }
    .login-footer p {
      color: #8B949E;
      font-size: 11px;
    }
  `]
})
export class LoginComponent {
  username = '';
  password = '';
  loading = false;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  onSubmit(): void {
    if (!this.username || !this.password) {
      this.errorMessage = 'Preencha todos os campos';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.authService.login(this.username, this.password).subscribe(success => {
      this.loading = false;
      if (success) {
        const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';
        this.router.navigateByUrl(returnUrl);
      } else {
        this.errorMessage = 'Credenciais inválidas';
      }
    });
  }
}
