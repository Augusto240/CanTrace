import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

interface NavItem {
  icon: string;
  label: string;
  route: string;
  badge?: number;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  navItems: NavItem[] = [
    { icon: '📊', label: 'Dashboard', route: '/dashboard' },
    { icon: '📡', label: 'Dispositivos', route: '/dispositivos' },
    { icon: '📈', label: 'Telemetria', route: '/telemetria' },
    { icon: '⚡', label: 'Alertas', route: '/alertas', badge: 3 },
    { icon: '📋', label: 'Auditoria', route: '/auditoria' },
  ];

  isCollapsed = false;

  toggleCollapse(): void {
    this.isCollapsed = !this.isCollapsed;
  }
}
