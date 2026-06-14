package br.edu.cantrace.alertas;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlertaService {

    private final AlertaRepository alertaRepository;

    public AlertaService(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    public AlertaAmbiental salvar(AlertaAmbiental alerta) {
        return alertaRepository.save(alerta);
    }

    @Transactional(readOnly = true)
    public List<AlertaAmbiental> buscarPorStatus(AlertaStatus status, int page, int size) {
        return alertaRepository.findByStatusOrderByCriadoEmDesc(status, PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public List<AlertaAmbiental> buscarPorDispositivo(UUID dispositivoId) {
        return alertaRepository.findByDispositivoIdOrderByCriadoEmDesc(dispositivoId);
    }

    @Transactional(readOnly = true)
    public List<AlertaAmbiental> buscarPorDispositivoEStatus(UUID dispositivoId, AlertaStatus status, int page, int size) {
        return alertaRepository.findByDispositivoIdAndStatus(dispositivoId, status, PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public long contarPorStatus(AlertaStatus status) {
        return alertaRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public long contarPorNivel(NivelAlerta nivel) {
        return alertaRepository.countByNivel(nivel);
    }

    public AlertaAmbiental resolver(UUID alertaId, String usuario) {
        AlertaAmbiental alerta = alertaRepository.findById(alertaId)
            .orElseThrow(() -> new IllegalArgumentException("Alerta nao encontrado: " + alertaId));

        alerta.resolver(usuario);
        return alertaRepository.save(alerta);
    }

    public AlertaAmbiental ignorar(UUID alertaId, String usuario) {
        AlertaAmbiental alerta = alertaRepository.findById(alertaId)
            .orElseThrow(() -> new IllegalArgumentException("Alerta nao encontrado: " + alertaId));

        alerta.ignorar(usuario);
        return alertaRepository.save(alerta);
    }
}
