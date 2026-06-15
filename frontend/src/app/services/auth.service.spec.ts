import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    sessionStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login successfully with valid credentials', () => {
    service.login('admin', 'admin123').subscribe(result => {
      expect(result).toBe(true);
      expect(service.isAuthenticated()).toBe(true);
      expect(service.getUser()?.username).toBe('admin');
      expect(service.getUser()?.role).toBe('ADMIN');
    });

    const req = httpMock.expectOne('/api/db/ping');
    expect(req.request.method).toBe('GET');
    req.flush('OK');
  });

  it('should fail login with invalid credentials', () => {
    service.login('wrong', 'wrong').subscribe(result => {
      expect(result).toBe(false);
      expect(service.isAuthenticated()).toBe(false);
    });

    const req = httpMock.expectOne('/api/db/ping');
    req.flush('Unauthorized', { status: 401, statusText: 'Unauthorized' });
  });

  it('should logout and clear session', () => {
    service.login('admin', 'admin123').subscribe();
    const req = httpMock.expectOne('/api/db/ping');
    req.flush('OK');

    service.logout();
    expect(service.isAuthenticated()).toBe(false);
    expect(service.getUser()).toBeNull();
  });

  it('should return operator role for non-admin users', () => {
    service.login('operator', 'operator123').subscribe(result => {
      expect(result).toBe(true);
      expect(service.getUser()?.role).toBe('OPERATOR');
    });

    const req = httpMock.expectOne('/api/db/ping');
    req.flush('OK');
  });

  it('should return null user when not authenticated', () => {
    expect(service.getUser()).toBeNull();
    expect(service.isAuthenticated()).toBe(false);
    expect(service.getToken()).toBeNull();
  });

  it('should check role correctly', () => {
    service.login('admin', 'admin123').subscribe();
    const req = httpMock.expectOne('/api/db/ping');
    req.flush('OK');

    expect(service.hasRole('ADMIN')).toBe(true);
    expect(service.hasRole('OPERATOR')).toBe(false);
  });
});
