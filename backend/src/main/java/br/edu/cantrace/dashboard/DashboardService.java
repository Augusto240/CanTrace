package br.edu.cantrace.dashboard;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.edu.cantrace.alertas.AlertaRepository;
import br.edu.cantrace.alertas.AlertaStatus;
import br.edu.cantrace.audit.AuditRepository;
import br.edu.cantrace.dispositivos.DispositivoRepository;
import br.edu.cantrace.dispositivos.DispositivoStatus;
import br.edu.cantrace.telemetria.LeituraAmbiental;
import br.edu.cantrace.telemetria.TelemetriaRepository;

@Service
public class DashboardService {

    private final DispositivoRepository dispositivoRepository;
    private final TelemetriaRepository telemetriaRepository;
    private final AlertaRepository alertaRepository;
    private final AuditRepository auditRepository;

    public DashboardService(DispositivoRepository dispositivoRepository,
                            TelemetriaRepository telemetriaRepository,
                            AlertaRepository alertaRepository,
                            AuditRepository auditRepository) {
        this.dispositivoRepository = dispositivoRepository;
        this.telemetriaRepository = telemetriaRepository;
        this.alertaRepository = alertaRepository;
        this.auditRepository = auditRepository;
    }

    public DashboardDTO buscarDashboard() {
        Instant agora = Instant.now();
        Instant ha24h = agora.minus(24, ChronoUnit.HOURS);
        Instant ha7d = agora.minus(7, ChronoUnit.DAYS);

        int dispositivosAtivo = dispositivoRepository.countByStatus(DispositivoStatus.ATIVO);
        int dispositivosInativo = dispositivoRepository.countByStatus(DispositivoStatus.INATIVO);
        int dispositivosManutencao = dispositivoRepository.countByStatus(DispositivoStatus.MANUTENCAO);
        int dispositivosOffline = dispositivoRepository.countByStatus(DispositivoStatus.OFFLINE);

        long leituras24h = telemetriaRepository.countByTimestampBetween(ha24h, agora);
        long leituras7d = telemetriaRepository.countByTimestampBetween(ha7d, agora);

        Double mediaTemperatura = telemetriaRepository.avgTemperaturaByTimestampBetween(ha24h, agora);
        Double mediaUmidade = telemetriaRepository.avgUmidadeByTimestampBetween(ha24h, agora);
        Double mediaLuminosidade = telemetriaRepository.avgLuminosidadeByTimestampBetween(ha24h, agora);

        long alertasAtivo = alertaRepository.countByStatus(AlertaStatus.ATIVO);
        long alertasResolvido = alertaRepository.countByStatus(AlertaStatus.RESOLVIDO);
        long alertasIgnorado = alertaRepository.countByStatus(AlertaStatus.IGNORADO);

        long registrosAuditoria = auditRepository.count();

        List<DashboardLeituraResumoDTO> ultimasLeituras = buscarUltimasLeituras();

        return new DashboardDTO(
            dispositivosAtivo,
            dispositivosInativo,
            dispositivosManutencao,
            dispositivosOffline,
            leituras24h,
            leituras7d,
            mediaTemperatura,
            mediaUmidade,
            mediaLuminosidade,
            alertasAtivo,
            alertasResolvido,
            alertasIgnorado,
            registrosAuditoria,
            ultimasLeituras
        );
    }

    private List<DashboardLeituraResumoDTO> buscarUltimasLeituras() {
        List<DashboardLeituraResumoDTO> resultado = new ArrayList<>();

        List<UUID> dispositivosAtivos = dispositivoRepository.findAll().stream()
            .filter(d -> d.getStatus() == DispositivoStatus.ATIVO)
            .map(d -> d.getId())
            .toList();

        for (UUID dispositivoId : dispositivosAtivos) {
            telemetriaRepository.findFirstByDispositivoIdOrderByTimestampDesc(dispositivoId)
                .ifPresent(leitura -> {
                    var dispositivo = dispositivoRepository.findById(dispositivoId);
                    dispositivo.ifPresent(d -> {
                        resultado.add(new DashboardLeituraResumoDTO(
                            d.getId(),
                            d.getNome(),
                            d.getArea(),
                            leitura.getTemperatura(),
                            leitura.getUmidade(),
                            leitura.getLuminosidade(),
                            leitura.getTimestamp()
                        ));
                    });
                });
        }

        return resultado;
    }
}
