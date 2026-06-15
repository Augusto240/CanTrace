package br.edu.cantrace.dashboard;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.edu.cantrace.alertas.AlertaRepository;
import br.edu.cantrace.alertas.AlertaStatus;
import br.edu.cantrace.audit.AuditRepository;
import br.edu.cantrace.dispositivos.DispositivoIoT;
import br.edu.cantrace.dispositivos.DispositivoRepository;
import br.edu.cantrace.dispositivos.DispositivoStatus;
import br.edu.cantrace.dispositivos.TipoSensor;
import br.edu.cantrace.telemetria.LeituraAmbiental;
import br.edu.cantrace.telemetria.OrigemLeitura;
import br.edu.cantrace.telemetria.TelemetriaRepository;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private DispositivoRepository dispositivoRepository;

    @Mock
    private TelemetriaRepository telemetriaRepository;

    @Mock
    private AlertaRepository alertaRepository;

    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private DashboardService dashboardService;

    private DispositivoIoT dispositivoAtivo;
    private LeituraAmbiental leitura;

    @BeforeEach
    void setUp() {
        dispositivoAtivo = new DispositivoIoT("DEV-001", "Sensor Sala A", "Sala A", List.of(TipoSensor.TEMPERATURA));
        dispositivoAtivo.setId(UUID.randomUUID());
        dispositivoAtivo.alterarStatus(DispositivoStatus.ATIVO);

        leitura = new LeituraAmbiental(
            dispositivoAtivo.getId(),
            25.0,
            60.0,
            450.0,
            OrigemLeitura.MQTT,
            Instant.now()
        );
    }

    @Test
    void deveRetornarDashboardComTodosOsKPIs() {
        when(dispositivoRepository.countByStatus(DispositivoStatus.ATIVO)).thenReturn(12);
        when(dispositivoRepository.countByStatus(DispositivoStatus.INATIVO)).thenReturn(3);
        when(dispositivoRepository.countByStatus(DispositivoStatus.MANUTENCAO)).thenReturn(2);
        when(dispositivoRepository.countByStatus(DispositivoStatus.OFFLINE)).thenReturn(1);

        when(telemetriaRepository.countByTimestampBetween(any(Instant.class), any(Instant.class)))
            .thenReturn(150L)
            .thenReturn(850L);

        when(telemetriaRepository.avgTemperaturaByTimestampBetween(any(Instant.class), any(Instant.class)))
            .thenReturn(24.5);
        when(telemetriaRepository.avgUmidadeByTimestampBetween(any(Instant.class), any(Instant.class)))
            .thenReturn(65.2);
        when(telemetriaRepository.avgLuminosidadeByTimestampBetween(any(Instant.class), any(Instant.class)))
            .thenReturn(450.0);

        when(alertaRepository.countByStatus(AlertaStatus.ATIVO)).thenReturn(8L);
        when(alertaRepository.countByStatus(AlertaStatus.RESOLVIDO)).thenReturn(15L);
        when(alertaRepository.countByStatus(AlertaStatus.IGNORADO)).thenReturn(3L);

        when(auditRepository.count()).thenReturn(250L);

        when(dispositivoRepository.findAll()).thenReturn(List.of(dispositivoAtivo));
        when(dispositivoRepository.findById(dispositivoAtivo.getId())).thenReturn(Optional.of(dispositivoAtivo));
        when(telemetriaRepository.findFirstByDispositivoIdOrderByTimestampDesc(dispositivoAtivo.getId()))
            .thenReturn(Optional.of(leitura));

        DashboardDTO dashboard = dashboardService.buscarDashboard();

        assertEquals(12, dashboard.totalDispositivosAtivo());
        assertEquals(3, dashboard.totalDispositivosInativo());
        assertEquals(2, dashboard.totalDispositivosManutencao());
        assertEquals(1, dashboard.totalDispositivosOffline());

        assertEquals(150L, dashboard.totalLeituras24h());
        assertEquals(850L, dashboard.totalLeituras7d());

        assertEquals(24.5, dashboard.mediaTemperatura24h(), 0.01);
        assertEquals(65.2, dashboard.mediaUmidade24h(), 0.01);
        assertEquals(450.0, dashboard.mediaLuminosidade24h(), 0.01);

        assertEquals(8L, dashboard.totalAlertasAtivo());
        assertEquals(15L, dashboard.totalAlertasResolvido());
        assertEquals(3L, dashboard.totalAlertasIgnorado());

        assertEquals(250L, dashboard.totalRegistrosAuditoria());

        assertFalse(dashboard.ultimasLeituras().isEmpty());
        assertEquals(1, dashboard.ultimasLeituras().size());
        assertEquals("Sala A", dashboard.ultimasLeituras().get(0).area());
    }

    @Test
    void deveRetornarDashboardComNenhumDado() {
        when(dispositivoRepository.countByStatus(any())).thenReturn(0);
        when(telemetriaRepository.countByTimestampBetween(any(), any())).thenReturn(0L);
        when(telemetriaRepository.avgTemperaturaByTimestampBetween(any(), any())).thenReturn(null);
        when(telemetriaRepository.avgUmidadeByTimestampBetween(any(), any())).thenReturn(null);
        when(telemetriaRepository.avgLuminosidadeByTimestampBetween(any(), any())).thenReturn(null);
        when(alertaRepository.countByStatus(any())).thenReturn(0L);
        when(auditRepository.count()).thenReturn(0L);
        when(dispositivoRepository.findAll()).thenReturn(List.of());

        DashboardDTO dashboard = dashboardService.buscarDashboard();

        assertEquals(0, dashboard.totalDispositivosAtivo());
        assertEquals(0L, dashboard.totalLeituras24h());
        assertNull(dashboard.mediaTemperatura24h());
        assertTrue(dashboard.ultimasLeituras().isEmpty());
    }

    @Test
    void deveFiltrarApenasDispositivosAtivosNasUltimasLeituras() {
        DispositivoIoT dispositivoInativo = new DispositivoIoT("DEV-002", "Sensor Sala B", "Sala B", List.of(TipoSensor.UMIDADE));
        dispositivoInativo.setId(UUID.randomUUID());
        dispositivoInativo.alterarStatus(DispositivoStatus.INATIVO);

        when(dispositivoRepository.countByStatus(any())).thenReturn(0);
        when(telemetriaRepository.countByTimestampBetween(any(), any())).thenReturn(0L);
        when(telemetriaRepository.avgTemperaturaByTimestampBetween(any(), any())).thenReturn(null);
        when(telemetriaRepository.avgUmidadeByTimestampBetween(any(), any())).thenReturn(null);
        when(telemetriaRepository.avgLuminosidadeByTimestampBetween(any(), any())).thenReturn(null);
        when(alertaRepository.countByStatus(any())).thenReturn(0L);
        when(auditRepository.count()).thenReturn(0L);

        when(dispositivoRepository.findAll()).thenReturn(List.of(dispositivoAtivo, dispositivoInativo));
        when(dispositivoRepository.findById(dispositivoAtivo.getId())).thenReturn(Optional.of(dispositivoAtivo));
        when(telemetriaRepository.findFirstByDispositivoIdOrderByTimestampDesc(dispositivoAtivo.getId()))
            .thenReturn(Optional.of(leitura));

        DashboardDTO dashboard = dashboardService.buscarDashboard();

        assertEquals(1, dashboard.ultimasLeituras().size());
        assertEquals("Sala A", dashboard.ultimasLeituras().get(0).area());
    }
}
