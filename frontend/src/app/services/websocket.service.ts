import { Injectable, OnDestroy } from '@angular/core';
import { Subject, BehaviorSubject } from 'rxjs';

export type ConnectionStatus = 'connected' | 'disconnected' | 'reconnecting';

export interface TelemetryEvent {
  dispositivoId: string;
  temperatura: number;
  umidade: number;
  luminosidade: number;
  timestamp: string;
}

export interface AlertEvent {
  id: string;
  titulo: string;
  nivel: string;
  dispositivoId: string;
  timestamp: string;
}

@Injectable({ providedIn: 'root' })
export class WebSocketService implements OnDestroy {
  private ws: WebSocket | null = null;
  private reconnectAttempts = 0;
  private maxReconnectAttempts = 10;
  private reconnectTimeout: any;

  telemetry$ = new Subject<TelemetryEvent>();
  alerts$ = new Subject<AlertEvent>();
  status$ = new BehaviorSubject<ConnectionStatus>('disconnected');

  connect(): void {
    if (this.ws?.readyState === WebSocket.OPEN) {
      return;
    }

    this.status$.next('reconnecting');
    
    try {
      const wsUrl = `ws://${window.location.host}/ws`;
      this.ws = new WebSocket(wsUrl);

      this.ws.onopen = () => {
        this.status$.next('connected');
        this.reconnectAttempts = 0;
        console.log('WebSocket connected');
      };

      this.ws.onmessage = (event) => {
        this.handleMessage(event.data);
      };

      this.ws.onclose = () => {
        this.status$.next('disconnected');
        this.scheduleReconnect();
      };

      this.ws.onerror = (error) => {
        console.error('WebSocket error:', error);
        this.ws?.close();
      };
    } catch (error) {
      console.error('WebSocket connection failed:', error);
      this.scheduleReconnect();
    }
  }

  disconnect(): void {
    if (this.reconnectTimeout) {
      clearTimeout(this.reconnectTimeout);
    }
    this.ws?.close();
    this.ws = null;
    this.status$.next('disconnected');
  }

  private handleMessage(data: string): void {
    try {
      const message = JSON.parse(data);
      
      switch (message.type) {
        case 'telemetry':
          this.telemetry$.next(message.payload);
          break;
        case 'alert':
          this.alerts$.next(message.payload);
          break;
      }
    } catch (error) {
      console.error('Failed to parse WebSocket message:', error);
    }
  }

  private scheduleReconnect(): void {
    if (this.reconnectAttempts >= this.maxReconnectAttempts) {
      console.error('Max reconnect attempts reached');
      return;
    }

    const delay = Math.min(1000 * Math.pow(2, this.reconnectAttempts), 30000);
    this.reconnectAttempts++;

    console.log(`Reconnecting in ${delay}ms (attempt ${this.reconnectAttempts})`);
    
    this.reconnectTimeout = setTimeout(() => {
      this.connect();
    }, delay);
  }

  ngOnDestroy(): void {
    this.disconnect();
  }
}
