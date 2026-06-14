package br.edu.cantrace.shared;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RequestContextServiceTest {

    @Test
    void deveCapturarIPAddress() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("192.168.1.100");
        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0");
        when(request.getHeader("X-User")).thenReturn("admin");
        when(request.getRequestURI()).thenReturn("/api/v1/lotes");
        when(request.getMethod()).thenReturn("POST");

        ServletRequestAttributes attrs = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attrs);

        RequestContextService service = new RequestContextService();

        assertEquals("192.168.1.100", service.getClientIp());
        assertEquals("Mozilla/5.0", service.getUserAgent());
        assertEquals("admin", service.getUsername());
        assertEquals("/api/v1/lotes", service.getUri());
        assertEquals("POST", service.getMethod());

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void deveRetornarDefaultsSemRequest() {
        RequestContextHolder.resetRequestAttributes();

        RequestContextService service = new RequestContextService();

        assertEquals("unknown", service.getClientIp());
        assertEquals("unknown", service.getUserAgent());
        assertEquals("system", service.getUsername());
        assertEquals("unknown", service.getUri());
        assertEquals("unknown", service.getMethod());
    }

    @Test
    void deveCapturarXForwardedFor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn("10.0.0.1, 192.168.1.1");

        ServletRequestAttributes attrs = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attrs);

        RequestContextService service = new RequestContextService();

        assertEquals("10.0.0.1", service.getClientIp());

        RequestContextHolder.resetRequestAttributes();
    }
}
