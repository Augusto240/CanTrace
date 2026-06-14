package br.edu.cantrace.dispositivos;

import java.util.List;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.cantrace.dispositivos.events.DispositivoAtualizadoEvent;
import br.edu.cantrace.dispositivos.events.DispositivoCadastradoEvent;
import br.edu.cantrace.dispositivos.events.DispositivoRemovidoEvent;
import br.edu.cantrace.dispositivos.events.DispositivoStatusAlteradoEvent;

@Service
@Transactional
public class DispositivoService {

    private final DispositivoRepository dispositivoRepository;
    private final ApplicationEventPublisher eventPublisher;

    public DispositivoService(DispositivoRepository dispositivoRepository,
                              ApplicationEventPublisher eventPublisher) {
        this.dispositivoRepository = dispositivoRepository;
        this.eventPublisher = eventPublisher;
    }

    public DispositivoIoT criar(DispositivoRequest request) {
        dispositivoRepository.findByDeviceCode(request.deviceCode())
            .ifPresent(d -> {
                throw new IllegalArgumentException("deviceCode ja existente: " + request.deviceCode());
            });

        DispositivoIoT dispositivo = new DispositivoIoT(
            request.deviceCode(),
            request.nome(),
            request.area(),
            request.tipoSensors()
        );

        DispositivoIoT saved = dispositivoRepository.save(dispositivo);

        eventPublisher.publishEvent(new DispositivoCadastradoEvent(
            this, saved.getId().toString(), saved.getDeviceCode(),
            saved.getArea(), saved.getTipoSensors()
        ));

        return saved;
    }

    @Transactional(readOnly = true)
    public List<DispositivoIoT> listar() {
        return dispositivoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public DispositivoIoT buscarPorId(UUID id) {
        return dispositivoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Dispositivo nao encontrado: " + id));
    }

    public DispositivoIoT atualizar(UUID id, DispositivoRequest request) {
        DispositivoIoT dispositivo = buscarPorId(id);

        dispositivoRepository.findByDeviceCode(request.deviceCode())
            .filter(existing -> !existing.getId().equals(id))
            .ifPresent(existing -> {
                throw new IllegalArgumentException("deviceCode ja existente: " + request.deviceCode());
            });

        dispositivo.atualizarDados(request.nome(), request.area(), request.tipoSensors());
        DispositivoIoT saved = dispositivoRepository.save(dispositivo);

        eventPublisher.publishEvent(new DispositivoAtualizadoEvent(
            this, saved.getId().toString(), saved.getDeviceCode(),
            saved.getNome(), saved.getArea(), saved.getTipoSensors()
        ));

        return saved;
    }

    public DispositivoIoT alterarStatus(UUID id, DispositivoStatus novoStatus) {
        DispositivoIoT dispositivo = buscarPorId(id);
        DispositivoStatus statusAnterior = dispositivo.getStatus();

        dispositivo.alterarStatus(novoStatus);
        DispositivoIoT saved = dispositivoRepository.save(dispositivo);

        eventPublisher.publishEvent(new DispositivoStatusAlteradoEvent(
            this, saved.getId().toString(), saved.getDeviceCode(),
            statusAnterior, novoStatus
        ));

        return saved;
    }

    public void remover(UUID id) {
        DispositivoIoT dispositivo = buscarPorId(id);
        String deviceCode = dispositivo.getDeviceCode();

        dispositivo.softDelete();
        dispositivoRepository.save(dispositivo);

        eventPublisher.publishEvent(new DispositivoRemovidoEvent(
            this, id.toString(), deviceCode
        ));
    }
}
