package br.edu.cantrace.telemetria;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.cantrace.shared.ErrorResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/telemetria")
public class TelemetriaController {

    private final TelemetriaService telemetriaService;

    public TelemetriaController(TelemetriaService telemetriaService) {
        this.telemetriaService = telemetriaService;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody TelemetriaRequest request) {
        try {
            LeituraAmbiental leitura = telemetriaService.registrar(request);
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(leitura.getId())
                .toUri();
            return ResponseEntity.created(location).body(TelemetriaResponse.fromEntity(leitura));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ErrorResponse.of(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<TelemetriaResponse>> listar(
            @RequestParam UUID dispositivoId,
            @RequestParam(required = false) String inicio,
            @RequestParam(required = false) String fim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<TelemetriaResponse> leituras = telemetriaService.buscarHistorico(dispositivoId, page, size).stream()
            .map(TelemetriaResponse::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(leituras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        return telemetriaService.buscarPorId(id)
            .map(leitura -> ResponseEntity.ok((Object) TelemetriaResponse.fromEntity(leitura)))
            .orElse(ResponseEntity.notFound().build());
    }
}
