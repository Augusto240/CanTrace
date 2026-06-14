package br.edu.cantrace.telemetria;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.cantrace.dispositivos.DispositivoIoT;
import br.edu.cantrace.dispositivos.DispositivoRepository;
import br.edu.cantrace.dispositivos.DispositivoStatus;
import br.edu.cantrace.telemetria.events.TelemetriaRecebidaEvent;

@Service
@Transactional
public class TelemetriaService {

    private final TelemetriaRepository telemetriaRepository;
    private final DispositivoRepository dispositivoRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TelemetriaService(TelemetriaRepository telemetriaRepository,
                             DispositivoRepository dispositivoRepository,
                             ApplicationEventPublisher eventPublisher) {
        this.telemetriaRepository = telemetriaRepository;
        this.dispositivoRepository = dispositivoRepository;
        this.eventPublisher = eventPublisher;
    }

    public LeituraAmbiental registrar(TelemetriaRequest request) {
        DispositivoIoT dispositivo = dispositivoRepository.findById(request.dispositivoId())
            .orElseThrow(() -> new IllegalArgumentException("Dispositivo nao encontrado: " + request.dispositivoId()));

        if (dispositivo.getStatus() == DispositivoStatus.INATIVO) {
            throw new IllegalArgumentException("Dispositivo esta inativo: " + request.dispositivoId());
        }

        validarTemperatura(request.temperatura());
        validarUmidade(request.umidade());
        validarLuminosidade(request.luminosidade());

        LeituraAmbiental leitura = new LeituraAmbiental(
            request.dispositivoId(),
            request.temperatura(),
            request.umidade(),
            request.luminosidade(),
            request.origem(),
            request.timestamp()
        );

        LeituraAmbiental saved = telemetriaRepository.save(leitura);

        eventPublisher.publishEvent(new TelemetriaRecebidaEvent(
            this, saved.getId().toString(), saved.getDispositivoId().toString(),
            saved.getTemperatura(), saved.getUmidade(), saved.getLuminosidade(),
            saved.getOrigem()
        ));

        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<LeituraAmbiental> buscarUltimaLeitura(UUID dispositivoId) {
        return telemetriaRepository.findFirstByDispositivoIdOrderByTimestampDesc(dispositivoId);
    }

    @Transactional(readOnly = true)
    public List<LeituraAmbiental> buscarHistorico(UUID dispositivoId, int page, int size) {
        return telemetriaRepository.findByDispositivoIdOrderByTimestampDesc(
            dispositivoId, PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Optional<LeituraAmbiental> buscarPorId(UUID id) {
        return telemetriaRepository.findById(id);
    }

    private void validarTemperatura(Double temperatura) {
        if (temperatura < -50 || temperatura > 100) {
            throw new IllegalArgumentException("Temperatura fora do limite: " + temperatura);
        }
    }

    private void validarUmidade(Double umidade) {
        if (umidade < 0 || umidade > 100) {
            throw new IllegalArgumentException("Umidade fora do limite: " + umidade);
        }
    }

    private void validarLuminosidade(Double luminosidade) {
        if (luminosidade < 0) {
            throw new IllegalArgumentException("Luminosidade nao pode ser negativa: " + luminosidade);
        }
    }
}
