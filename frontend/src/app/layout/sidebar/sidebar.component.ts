import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService, User } from '../../services/auth.service';

interface NavItem {
  icon: string;
  label: string;
  route: string;
  badge?: number;
  adminOnly?: boolean;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  navItems: NavItem[] = [
    { icon: '📊', label: 'Dashboard', route: '/dashboard' },
    { icon: '📡', label: 'Dispositivos', route: '/dispositivos' },
    { icon: '📈', label: 'Telemetria', route: '/telemetria' },
    { icon: '⚡', label: 'Alertas', route: '/alertas', badge: 0 },
    { icon: '📋', label: 'Auditoria', route: '/auditoria', adminOnly: true },
  ];

  isCollapsed = false;
  user: User | null = null;
  visibleNavItems: NavItem[] = [];

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getUser();
    this.visibleNavItems = this.navItems.filter(item => {
      if (item.adminOnly && this.user?.role !== 'ADMIN') return false;
      return true;
    });
  }

  toggleCollapse(): void {
    this.isCollapsed = !this.isCollapsed;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
