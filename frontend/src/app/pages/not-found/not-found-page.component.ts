import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-not-found-page',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="not-found-container">
      <div class="not-found-card">
        <div class="error-code">404</div>
        <h1>Página Não Encontrada</h1>
        <p>O recurso solicitado não existe ou foi movido.</p>
        <a routerLink="/dashboard" class="btn-back">← Voltar ao Dashboard</a>
      </div>
    </div>
  `,
  styles: [`
    .not-found-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #0A1628;
    }
    .not-found-card {
      text-align: center;
      padding: 48px;
    }
    .error-code {
      font-size: 120px;
      font-weight: 700;
      color: #1B2838;
      line-height: 1;
    }
    h1 {
      color: #E6EDF3;
      font-size: 24px;
      margin: 16px 0 8px;
    }
    p {
      color: #8B949E;
      margin-bottom: 24px;
    }
    .btn-back {
      display: inline-block;
      padding: 12px 24px;
      background: linear-gradient(135deg, #00E676, #4A7C59);
      color: #0A1628;
      text-decoration: none;
      border-radius: 8px;
      font-weight: 600;
    }
  `]
})
export class NotFoundPageComponent {}
