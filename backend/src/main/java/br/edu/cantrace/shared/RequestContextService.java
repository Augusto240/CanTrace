package br.edu.cantrace.shared;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class RequestContextService {

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attrs =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs != null ? attrs.getRequest() : null;
    }

    public String getClientIp() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) return "unknown";

        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    public String getUserAgent() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) return "unknown";

        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? userAgent : "unknown";
    }

    public String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getName() != null) {
            return auth.getName();
        }

        HttpServletRequest request = getCurrentRequest();
        if (request == null) return "system";

        String xUser = request.getHeader("X-User");
        return xUser != null ? xUser : "system";
    }

    public String getRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getAuthorities() != null) {
            return auth.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring(5))
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    public String getUri() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) return "unknown";

        return request.getRequestURI();
    }

    public String getMethod() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) return "unknown";

        return request.getMethod();
    }
}
