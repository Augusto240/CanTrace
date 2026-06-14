package br.edu.cantrace.alertas;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertaServiceTest {

    @Mock
    private AlertaRepository alertaRepository;

    @InjectMocks
    private AlertaService alertaService;

    private AlertaAmbiental alerta;

    @BeforeEach
    void setUp() {
        alerta = new AlertaAmbiental(
            "Teste Alerta", "Mensagem teste", NivelAlerta.WARN,
            TipoAlerta.TEMPERATURA_ALTA, UUID.randomUUID(), UUID.randomUUID()
        );
    }

    @Test
    void deveSalvarAlerta() {
        when(alertaRepository.save(any(AlertaAmbiental.class))).thenReturn(alerta);

        AlertaAmbiental resultado = alertaService.salvar(alerta);

        assertNotNull(resultado);
        verify(alertaRepository, times(1)).save(alerta);
    }

    @Test
    void deveResolverAlerta() {
        when(alertaRepository.findById(alerta.getId())).thenReturn(Optional.of(alerta));
        when(alertaRepository.save(any(AlertaAmbiental.class))).thenReturn(alerta);

        AlertaAmbiental resultado = alertaService.resolver(alerta.getId(), "admin");

        assertEquals(AlertaStatus.RESOLVIDO, resultado.getStatus());
        assertEquals("admin", resultado.getResolvidoPor());
    }

    @Test
    void deveLancarExcecaoQuandoAlertaNaoEncontrado() {
        when(alertaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> alertaService.resolver(UUID.randomUUID(), "admin"));
    }

    @Test
    void deveLancarExcecaoQuandoAlertaNaoAtivo() {
        alerta.resolver("admin");
        when(alertaRepository.findById(alerta.getId())).thenReturn(Optional.of(alerta));

        assertThrows(IllegalStateException.class,
            () -> alertaService.resolver(alerta.getId(), "admin"));
    }
}
