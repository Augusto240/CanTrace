package br.edu.cantrace.lotes;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.cantrace.shared.ErrorResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/lotes")
public class LoteController {

    private final LoteService loteService;

    public LoteController(LoteService loteService) {
        this.loteService = loteService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody LoteRequest request) {
        try {
            Lote lote = loteService.criar(request);
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(lote.getId())
                .toUri();
            return ResponseEntity.created(location).body(LoteResponse.fromEntity(lote));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ErrorResponse.of(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<LoteResponse>> listar() {
        List<LoteResponse> lotes = loteService.listar().stream()
            .map(LoteResponse::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(lotes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        try {
            Lote lote = loteService.buscarPorId(id);
            return ResponseEntity.ok(LoteResponse.fromEntity(lote));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable UUID id, @Valid @RequestBody LoteRequest request) {
        try {
            Lote lote = loteService.atualizar(id, request);
            return ResponseEntity.ok(LoteResponse.fromEntity(lote));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("nao encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(ErrorResponse.of(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable UUID id) {
        try {
            loteService.remover(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
