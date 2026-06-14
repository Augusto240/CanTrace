package br.edu.cantrace.alertas;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/alertas")
public class AlertaController {

    private final AlertaService alertaService;
    private final AlertaRepository alertaRepository;

    public AlertaController(AlertaService alertaService, AlertaRepository alertaRepository) {
        this.alertaService = alertaService;
        this.alertaRepository = alertaRepository;
    }

    @GetMapping
    public ResponseEntity<List<AlertaAmbiental>> listar(
            @RequestParam(required = false) AlertaStatus status,
            @RequestParam(required = false) NivelAlerta nivel,
            @RequestParam(required = false) UUID dispositivoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Sort sort = Sort.by(Sort.Direction.DESC, "criadoEm");
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<AlertaAmbiental> spec = Specification.where(null);

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (nivel != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("nivel"), nivel));
        }
        if (dispositivoId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("dispositivoId"), dispositivoId));
        }

        List<AlertaAmbiental> alertas = alertaRepository.findAll(spec, pageable).getContent();
        return ResponseEntity.ok(alertas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaAmbiental> buscarPorId(@PathVariable UUID id) {
        return alertaRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/resolver")
    public ResponseEntity<AlertaAmbiental> resolver(@PathVariable UUID id,
            @RequestBody(required = false) Map<String, String> body) {
        String usuario = body != null ? body.getOrDefault("usuario", "sistema") : "sistema";
        try {
            AlertaAmbiental alerta = alertaService.resolver(id, usuario);
            return ResponseEntity.ok(alerta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/ignorar")
    public ResponseEntity<AlertaAmbiental> ignorar(@PathVariable UUID id,
            @RequestBody(required = false) Map<String, String> body) {
        String usuario = body != null ? body.getOrDefault("usuario", "sistema") : "sistema";
        try {
            AlertaAmbiental alerta = alertaService.ignorar(id, usuario);
            return ResponseEntity.ok(alerta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
