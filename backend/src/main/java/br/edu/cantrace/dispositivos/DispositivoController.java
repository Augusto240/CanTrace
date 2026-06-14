package br.edu.cantrace.dispositivos;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/v1/dispositivos")
public class DispositivoController {

    private final DispositivoService dispositivoService;

    public DispositivoController(DispositivoService dispositivoService) {
        this.dispositivoService = dispositivoService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody DispositivoRequest request) {
        try {
            DispositivoIoT dispositivo = dispositivoService.criar(request);
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dispositivo.getId())
                .toUri();
            return ResponseEntity.created(location).body(DispositivoResponse.fromEntity(dispositivo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ErrorResponse.of(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<DispositivoResponse>> listar() {
        List<DispositivoResponse> dispositivos = dispositivoService.listar().stream()
            .map(DispositivoResponse::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dispositivos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        try {
            DispositivoIoT dispositivo = dispositivoService.buscarPorId(id);
            return ResponseEntity.ok(DispositivoResponse.fromEntity(dispositivo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable UUID id, @Valid @RequestBody DispositivoRequest request) {
        try {
            DispositivoIoT dispositivo = dispositivoService.atualizar(id, request);
            return ResponseEntity.ok(DispositivoResponse.fromEntity(dispositivo));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("nao encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(ErrorResponse.of(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> alterarStatus(@PathVariable UUID id, @Valid @RequestBody DispositivoStatusRequest request) {
        try {
            DispositivoIoT dispositivo = dispositivoService.alterarStatus(id, request.status());
            return ResponseEntity.ok(DispositivoResponse.fromEntity(dispositivo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable UUID id) {
        try {
            dispositivoService.remover(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
