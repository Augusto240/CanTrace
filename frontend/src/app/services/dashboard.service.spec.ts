import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { DashboardService } from './dashboard.service';

describe('DashboardService', () => {
  let service: DashboardService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [DashboardService]
    });
    service = TestBed.inject(DashboardService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch dashboard data', () => {
    const mockData = {
      totalDispositivosAtivo: 12,
      totalAlertasAtivo: 5
    };

    service.getDashboard().subscribe(data => {
      expect(data).toEqual(mockData);
    });

    const req = httpMock.expectOne('/api/v1/dashboard');
    expect(req.request.method).toBe('GET');
    req.flush(mockData);
  });
});