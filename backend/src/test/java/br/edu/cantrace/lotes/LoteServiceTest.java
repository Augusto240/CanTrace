package br.edu.cantrace.lotes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoteServiceTest {

    @Mock
    private LoteRepository loteRepository;

    @InjectMocks
    private LoteService loteService;

    private LoteRequest createValidRequest() {
        return new LoteRequest(
            "LOT-001",
            "Produto Teste",
            LocalDate.now(),
            LocalDate.now().plusDays(30),
            new BigDecimal("100.00"),
            "Responsavel Teste",
            "Notas teste"
        );
    }

    @Test
    void deveCriarLoteValido() {
        when(loteRepository.findByCodigo("LOT-001")).thenReturn(Optional.empty());
        when(loteRepository.save(any(Lote.class))).thenAnswer(invocation -> {
            Lote lote = invocation.getArgument(0);
            lote.setId(UUID.randomUUID());
            return lote;
        });

        Lote lote = loteService.criar(createValidRequest());

        assertNotNull(lote);
        assertEquals("LOT-001", lote.getCodigo());
        assertEquals(LoteStatus.RASCUNHO, lote.getStatus());
        verify(loteRepository).save(any(Lote.class));
    }

    @Test
    void deveLancarExcecaoParaCodigoDuplicado() {
        when(loteRepository.findByCodigo("LOT-001")).thenReturn(Optional.of(new Lote()));

        assertThrows(IllegalArgumentException.class, () -> loteService.criar(createValidRequest()));
    }

    @Test
    void deveLancarExcecaoParaDataValidadeAnterior() {
        when(loteRepository.findByCodigo("LOT-001")).thenReturn(Optional.empty());

        LoteRequest request = new LoteRequest(
            "LOT-001",
            "Produto Teste",
            LocalDate.now(),
            LocalDate.now().minusDays(1),
            new BigDecimal("100.00"),
            "Responsavel Teste",
            null
        );

        assertThrows(IllegalArgumentException.class, () -> loteService.criar(request));
    }

    @Test
    void deveBuscarLotePorId() {
        UUID id = UUID.randomUUID();
        Lote lote = new Lote();
        lote.setId(id);
        lote.setCodigo("LOT-001");

        when(loteRepository.findById(id)).thenReturn(Optional.of(lote));

        Lote result = loteService.buscarPorId(id);

        assertNotNull(result);
        assertEquals("LOT-001", result.getCodigo());
    }

    @Test
    void deveLancarExcecaoParaLoteInexistente() {
        UUID id = UUID.randomUUID();
        when(loteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> loteService.buscarPorId(id));
    }

    @Test
    void deveRemoverLoteComSoftDelete() {
        UUID id = UUID.randomUUID();
        Lote lote = new Lote();
        lote.setId(id);
        lote.setStatus(LoteStatus.ATIVO);

        when(loteRepository.findById(id)).thenReturn(Optional.of(lote));
        when(loteRepository.save(any(Lote.class))).thenAnswer(invocation -> invocation.getArgument(0));

        loteService.remover(id);

        assertEquals(LoteStatus.DESCARTADO, lote.getStatus());
        verify(loteRepository).save(any(Lote.class));
    }
}
