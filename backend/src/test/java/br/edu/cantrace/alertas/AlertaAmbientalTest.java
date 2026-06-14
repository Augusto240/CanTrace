package br.edu.cantrace.alertas;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AlertaAmbientalTest {

    @Test
    void deveCriarAlertaComTodosCampos() {
        UUID leituraId = UUID.randomUUID();
        UUID dispositivoId = UUID.randomUUID();

        AlertaAmbiental alerta = new AlertaAmbiental(
            "Temperatura Alta",
            "Temperatura atingiu 30C",
            NivelAlerta.WARN,
            TipoAlerta.TEMPERATURA_ALTA,
            dispositivoId,
            leituraId
        );

        assertNotNull(alerta.getId());
        assertEquals("Temperatura Alta", alerta.getTitulo());
        assertEquals("Temperatura atingiu 30C", alerta.getMensagem());
        assertEquals(NivelAlerta.WARN, alerta.getNivel());
        assertEquals(TipoAlerta.TEMPERATURA_ALTA, alerta.getTipo());
        assertEquals(dispositivoId, alerta.getDispositivoId());
        assertEquals(leituraId, alerta.getLeituraId());
        assertEquals(AlertaStatus.ATIVO, alerta.getStatus());
        assertNotNull(alerta.getCriadoEm());
        assertNull(alerta.getResolvidoEm());
        assertNull(alerta.getResolvidoPor());
    }

    @Test
    void deveResolverAlerta() {
        AlertaAmbiental alerta = new AlertaAmbiental(
            "Teste", "mensagem", NivelAlerta.WARN, TipoAlerta.TEMPERATURA_ALTA,
            UUID.randomUUID(), UUID.randomUUID()
        );

        alerta.resolver("admin");

        assertEquals(AlertaStatus.RESOLVIDO, alerta.getStatus());
        assertNotNull(alerta.getResolvidoEm());
        assertEquals("admin", alerta.getResolvidoPor());
    }

    @Test
    void deveIgnorarAlerta() {
        AlertaAmbiental alerta = new AlertaAmbiental(
            "Teste", "mensagem", NivelAlerta.INFO, TipoAlerta.LUMINOSIDADE_ALTA,
            UUID.randomUUID(), UUID.randomUUID()
        );

        alerta.ignorar("operator");

        assertEquals(AlertaStatus.IGNORADO, alerta.getStatus());
        assertNotNull(alerta.getResolvidoEm());
        assertEquals("operator", alerta.getResolvidoPor());
    }

    @Test
    void naoDeveResolverAlertaJaResolvido() {
        AlertaAmbiental alerta = new AlertaAmbiental(
            "Teste", "mensagem", NivelAlerta.WARN, TipoAlerta.TEMPERATURA_ALTA,
            UUID.randomUUID(), UUID.randomUUID()
        );

        alerta.resolver("admin");
        assertThrows(IllegalStateException.class, () -> alerta.resolver("admin"));
    }

    @Test
    void implementaAuditable() {
        UUID leituraId = UUID.randomUUID();
        AlertaAmbiental alerta = new AlertaAmbiental(
            "Teste", "mensagem", NivelAlerta.WARN, TipoAlerta.TEMPERATURA_ALTA,
            UUID.randomUUID(), leituraId
        );

        assertTrue(alerta instanceof br.edu.cantrace.audit.Auditable);
        assertEquals(alerta.getId().toString(), alerta.getAuditableId());
        assertEquals("AlertaAmbiental", alerta.getAuditableEntityName());
    }
}
