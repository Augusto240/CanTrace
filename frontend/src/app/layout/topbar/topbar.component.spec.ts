import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TopbarComponent } from './topbar.component';

describe('TopbarComponent', () => {
  let component: TopbarComponent;
  let fixture: ComponentFixture<TopbarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TopbarComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(TopbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display page title', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.title')?.textContent).toContain('Dashboard Operacional');
  });

  it('should show MQTT status', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.mqtt-status')?.textContent).toContain('MQTT Desconectado');
  });

  it('should show connected MQTT status', () => {
    fixture.componentRef.setInput('mqttStatus', 'connected');
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.mqtt-status')?.textContent).toContain('MQTT Conectado');
  });
});
