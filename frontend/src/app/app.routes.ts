import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';

export const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadComponent: () => import('./dashboard/dashboard-page/dashboard-page.component')
          .then(m => m.DashboardPageComponent)
      },
      {
        path: 'dispositivos',
        loadComponent: () => import('./dashboard/dashboard-page/dashboard-page.component')
          .then(m => m.DashboardPageComponent)
      },
      {
        path: 'telemetria',
        loadComponent: () => import('./dashboard/dashboard-page/dashboard-page.component')
          .then(m => m.DashboardPageComponent)
      },
      {
        path: 'alertas',
        loadComponent: () => import('./dashboard/dashboard-page/dashboard-page.component')
          .then(m => m.DashboardPageComponent)
      },
      {
        path: 'auditoria',
        loadComponent: () => import('./dashboard/dashboard-page/dashboard-page.component')
          .then(m => m.DashboardPageComponent)
      },
    ]
  }
];
