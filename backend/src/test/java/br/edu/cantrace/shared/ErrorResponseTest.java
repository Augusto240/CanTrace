package br.edu.cantrace.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void deveCriarErrorResponseCompleto() {
        ErrorResponse response = new ErrorResponse(
            404,
            "Not Found",
            "Lote nao encontrado",
            "/api/v1/lotes/123",
            LocalDateTime.of(2026, 6, 14, 10, 0, 0)
        );

        assertEquals(404, response.status());
        assertEquals("Not Found", response.error());
        assertEquals("Lote nao encontrado", response.message());
        assertEquals("/api/v1/lotes/123", response.path());
        assertNotNull(response.timestamp());
    }

    @Test
    void deveCriarErrorResponseSemPath() {
        ErrorResponse response = ErrorResponse.of(400, "Bad Request", "Dados invalidos");

        assertEquals(400, response.status());
        assertEquals("Bad Request", response.error());
        assertEquals("Dados invalidos", response.message());
        assertNull(response.path());
    }
}
