package br.edu.cantrace.dispositivos;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import br.edu.cantrace.dispositivos.events.DispositivoCadastradoEvent;
import br.edu.cantrace.dispositivos.events.DispositivoRemovidoEvent;
import br.edu.cantrace.dispositivos.events.DispositivoStatusAlteradoEvent;

@ExtendWith(MockitoExtension.class)
class DispositivoServiceTest {

    @Mock
    private DispositivoRepository dispositivoRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private DispositivoService dispositivoService;

    private DispositivoIoT dispositivo;
    private DispositivoRequest request;

    @BeforeEach
    void setUp() {
        dispositivo = new DispositivoIoT("cantrace-iot-01", "Sensor Sala A", "area-01",
            Arrays.asList(TipoSensor.TEMPERATURA, TipoSensor.UMIDADE));
        dispositivo.setId(UUID.randomUUID());

        request = new DispositivoRequest("cantrace-iot-01", "Sensor Sala A", "area-01",
            Arrays.asList(TipoSensor.TEMPERATURA, TipoSensor.UMIDADE));
    }

    @Test
    void criar_DeveCriarDispositivo_ComEvent() {
        when(dispositivoRepository.findByDeviceCode("cantrace-iot-01")).thenReturn(Optional.empty());
        when(dispositivoRepository.save(any(DispositivoIoT.class))).thenReturn(dispositivo);

        DispositivoIoT result = dispositivoService.criar(request);

        assertNotNull(result);
        assertEquals("cantrace-iot-01", result.getDeviceCode());
        verify(eventPublisher).publishEvent(any(DispositivoCadastradoEvent.class));
    }

    @Test
    void criar_DeveLancarExcecao_SeDeviceCodeExistente() {
        when(dispositivoRepository.findByDeviceCode("cantrace-iot-01")).thenReturn(Optional.of(dispositivo));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> dispositivoService.criar(request));

        assertTrue(exception.getMessage().contains("deviceCode ja existente"));
    }

    @Test
    void listar_DeveRetornarLista() {
        when(dispositivoRepository.findAll()).thenReturn(Arrays.asList(dispositivo));

        List<DispositivoIoT> result = dispositivoService.listar();

        assertEquals(1, result.size());
    }

    @Test
    void buscarPorId_DeveRetornarDispositivo() {
        when(dispositivoRepository.findById(dispositivo.getId())).thenReturn(Optional.of(dispositivo));

        DispositivoIoT result = dispositivoService.buscarPorId(dispositivo.getId());

        assertEquals("cantrace-iot-01", result.getDeviceCode());
    }

    @Test
    void buscarPorId_DeveLancarExcecao_SeNaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(dispositivoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> dispositivoService.buscarPorId(id));
    }

    @Test
    void alterarStatus_DeveAlterar_ComEvento() {
        when(dispositivoRepository.findById(dispositivo.getId())).thenReturn(Optional.of(dispositivo));
        when(dispositivoRepository.save(any(DispositivoIoT.class))).thenReturn(dispositivo);

        DispositivoIoT result = dispositivoService.alterarStatus(dispositivo.getId(), DispositivoStatus.MANUTENCAO);

        assertEquals(DispositivoStatus.MANUTENCAO, result.getStatus());
        verify(eventPublisher).publishEvent(any(DispositivoStatusAlteradoEvent.class));
    }

    @Test
    void remover_DeveSoftDelete_ComEvento() {
        when(dispositivoRepository.findById(dispositivo.getId())).thenReturn(Optional.of(dispositivo));
        when(dispositivoRepository.save(any(DispositivoIoT.class))).thenReturn(dispositivo);

        dispositivoService.remover(dispositivo.getId());

        verify(dispositivoRepository).save(any(DispositivoIoT.class));
        verify(eventPublisher).publishEvent(any(DispositivoRemovidoEvent.class));
    }
}
