import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { Subject, takeUntil } from 'rxjs';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { TopbarComponent } from '../topbar/topbar.component';
import { WebSocketService } from '../../services/websocket.service';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, SidebarComponent, TopbarComponent],
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.scss']
})
export class MainLayoutComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  
  title = 'Dashboard Operacional';
  lastUpdate = new Date();
  mqttStatus: 'connected' | 'disconnected' | 'reconnecting' = 'disconnected';

  constructor(
    private websocketService: WebSocketService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.websocketService.status$
      .pipe(takeUntil(this.destroy$))
      .subscribe(status => {
        this.mqttStatus = status;
      });

    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      takeUntil(this.destroy$)
    ).subscribe(() => {
      this.updateTitle();
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onRefresh(): void {
    this.lastUpdate = new Date();
  }

  private updateTitle(): void {
    const url = this.router.url;
    if (url.includes('/dispositivos')) this.title = 'Dispositivos IoT';
    else if (url.includes('/telemetria')) this.title = 'Telemetria Ambiental';
    else if (url.includes('/alertas')) this.title = 'Alertas Ambientais';
    else if (url.includes('/auditoria')) this.title = 'Auditoria';
    else this.title = 'Dashboard Operacional';
  }
}
