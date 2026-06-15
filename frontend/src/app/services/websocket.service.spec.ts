import { TestBed } from '@angular/core/testing';
import { WebSocketService } from './websocket.service';

describe('WebSocketService', () => {
  let service: WebSocketService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [WebSocketService]
    });
    service = TestBed.inject(WebSocketService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have status$ observable', () => {
    expect(service.status$).toBeTruthy();
  });

  it('should have telemetry$ observable', () => {
    expect(service.telemetry$).toBeTruthy();
  });

  it('should have alerts$ observable', () => {
    expect(service.alerts$).toBeTruthy();
  });
});