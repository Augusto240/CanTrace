import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component')
      .then(m => m.LoginComponent)
  },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadComponent: () => import('./dashboard/dashboard-page/dashboard-page.component')
          .then(m => m.DashboardPageComponent)
      },
      {
        path: 'dispositivos',
        loadComponent: () => import('./pages/dispositivos/dispositivos-page.component')
          .then(m => m.DispositivosPageComponent)
      },
      {
        path: 'telemetria',
        loadComponent: () => import('./pages/telemetria/telemetria-page.component')
          .then(m => m.TelemetriaPageComponent)
      },
      {
        path: 'alertas',
        loadComponent: () => import('./pages/alertas/alertas-page.component')
          .then(m => m.AlertasPageComponent)
      },
      {
        path: 'auditoria',
        loadComponent: () => import('./pages/auditoria/auditoria-page.component')
          .then(m => m.AuditoriaPageComponent)
      },
    ]
  },
  {
    path: '**',
    loadComponent: () => import('./pages/not-found/not-found-page.component')
      .then(m => m.NotFoundPageComponent)
  }
];
