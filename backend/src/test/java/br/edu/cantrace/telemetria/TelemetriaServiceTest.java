package br.edu.cantrace.telemetria;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import br.edu.cantrace.dispositivos.DispositivoIoT;
import br.edu.cantrace.dispositivos.DispositivoRepository;
import br.edu.cantrace.dispositivos.DispositivoStatus;
import br.edu.cantrace.telemetria.events.TelemetriaRecebidaEvent;

@ExtendWith(MockitoExtension.class)
class TelemetriaServiceTest {

    @Mock
    private TelemetriaRepository telemetriaRepository;

    @Mock
    private DispositivoRepository dispositivoRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TelemetriaService telemetriaService;

    private DispositivoIoT dispositivo;
    private TelemetriaRequest request;

    @BeforeEach
    void setUp() {
        dispositivo = new DispositivoIoT("cantrace-iot-01", "Sensor Sala A", "area-01", null);
        dispositivo.setId(UUID.randomUUID());
        dispositivo.setStatus(DispositivoStatus.ATIVO);

        request = new TelemetriaRequest(
            dispositivo.getId(),
            25.0,
            60.0,
            500.0,
            OrigemLeitura.MANUAL,
            Instant.now()
        );
    }

    @Test
    void registrar_DeveRegistrarLeitura_ComEvento() {
        when(dispositivoRepository.findById(dispositivo.getId())).thenReturn(Optional.of(dispositivo));
        when(telemetriaRepository.save(any(LeituraAmbiental.class))).thenAnswer(invocation -> {
            LeituraAmbiental l = invocation.getArgument(0);
            l.setId(UUID.randomUUID());
            return l;
        });

        LeituraAmbiental result = telemetriaService.registrar(request);

        assertNotNull(result);
        assertEquals(25.0, result.getTemperatura());
        verify(eventPublisher).publishEvent(any(TelemetriaRecebidaEvent.class));
    }

    @Test
    void registrar_DeveLancarExcecao_SeDispositivoNaoEncontrado() {
        UUID idInexistente = UUID.randomUUID();
        TelemetriaRequest req = new TelemetriaRequest(idInexistente, 25.0, 60.0, 500.0, OrigemLeitura.MANUAL, Instant.now());
        when(dispositivoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> telemetriaService.registrar(req));
    }

    @Test
    void registrar_DeveLancarExcecao_SeDispositivoInativo() {
        dispositivo.setStatus(DispositivoStatus.INATIVO);
        when(dispositivoRepository.findById(dispositivo.getId())).thenReturn(Optional.of(dispositivo));

        assertThrows(IllegalArgumentException.class, () -> telemetriaService.registrar(request));
    }

    @Test
    void registrar_DeveLancarExcecao_SeTemperaturaForaLimite() {
        TelemetriaRequest req = new TelemetriaRequest(dispositivo.getId(), 150.0, 60.0, 500.0, OrigemLeitura.MANUAL, Instant.now());
        when(dispositivoRepository.findById(dispositivo.getId())).thenReturn(Optional.of(dispositivo));

        assertThrows(IllegalArgumentException.class, () -> telemetriaService.registrar(req));
    }

    @Test
    void registrar_DeveLancarExcecao_SeUmidadeForaLimite() {
        TelemetriaRequest req = new TelemetriaRequest(dispositivo.getId(), 25.0, 150.0, 500.0, OrigemLeitura.MANUAL, Instant.now());
        when(dispositivoRepository.findById(dispositivo.getId())).thenReturn(Optional.of(dispositivo));

        assertThrows(IllegalArgumentException.class, () -> telemetriaService.registrar(req));
    }

    @Test
    void registrar_DeveLancarExcecao_SeLuminosidadeNegativa() {
        TelemetriaRequest req = new TelemetriaRequest(dispositivo.getId(), 25.0, 60.0, -10.0, OrigemLeitura.MANUAL, Instant.now());
        when(dispositivoRepository.findById(dispositivo.getId())).thenReturn(Optional.of(dispositivo));

        assertThrows(IllegalArgumentException.class, () -> telemetriaService.registrar(req));
    }

    @Test
    void buscarUltimaLeitura_DeveRetornarLeitura() {
        LeituraAmbiental leitura = new LeituraAmbiental(dispositivo.getId(), 25.0, 60.0, 500.0, OrigemLeitura.MANUAL, Instant.now());
        when(telemetriaRepository.findFirstByDispositivoIdOrderByTimestampDesc(dispositivo.getId()))
            .thenReturn(Optional.of(leitura));

        Optional<LeituraAmbiental> result = telemetriaService.buscarUltimaLeitura(dispositivo.getId());

        assertTrue(result.isPresent());
        assertEquals(25.0, result.get().getTemperatura());
    }

    @Test
    void buscarPorId_DeveRetornarLeitura() {
        LeituraAmbiental leitura = new LeituraAmbiental(dispositivo.getId(), 25.0, 60.0, 500.0, OrigemLeitura.MANUAL, Instant.now());
        leitura.setId(UUID.randomUUID());
        when(telemetriaRepository.findById(leitura.getId())).thenReturn(Optional.of(leitura));

        Optional<LeituraAmbiental> result = telemetriaService.buscarPorId(leitura.getId());

        assertTrue(result.isPresent());
        assertEquals(leitura.getId(), result.get().getId());
    }
}
