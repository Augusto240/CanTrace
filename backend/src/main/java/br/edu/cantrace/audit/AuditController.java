package br.edu.cantrace.audit;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auditoria")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<Page<AuditLog>> findAll(AuditFilter filter) {
        Page<AuditLog> result = auditService.findAll(filter);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLog> findById(@PathVariable UUID id) {
        return auditService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/entidade/{entidade}")
    public ResponseEntity<List<AuditLog>> findByEntidade(@PathVariable String entidade) {
        List<AuditLog> result = auditService.findByEntidade(entidade);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<AuditStats> getEstatisticas() {
        AuditStats stats = auditService.getEstatisticas();
        return ResponseEntity.ok(stats);
    }
}
