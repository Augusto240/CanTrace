package br.edu.cantrace.audit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.cantrace.shared.RequestContextService;

@Aspect
@Component
public class AuditInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuditInterceptor.class);

    private final AuditRepository auditRepository;
    private final RequestContextService requestContextService;
    private final ObjectMapper objectMapper;

    public AuditInterceptor(AuditRepository auditRepository,
                           RequestContextService requestContextService,
                           ObjectMapper objectMapper) {
        this.auditRepository = auditRepository;
        this.requestContextService = requestContextService;
        this.objectMapper = objectMapper;
    }

    @AfterReturning(pointcut = "execution(* org.springframework.data.jpa.repository.JpaRepository+.save(..))", returning = "result")
    public void interceptSave(JoinPoint joinPoint, Object result) {
        if (result instanceof AuditLog) {
            return;
        }

        try {
            String entityName;
            String entityId;
            String dadosNovos;

            if (result instanceof Auditable auditable) {
                entityName = auditable.getAuditableEntityName();
                entityId = auditable.getAuditableId() != null
                    ? auditable.getAuditableId().toString() : "unknown";
                dadosNovos = objectMapper.writeValueAsString(auditable.getAuditableData());
            } else {
                entityName = result.getClass().getSimpleName();
                entityId = extractIdFallback(result);
                dadosNovos = serializeToJsonFallback(result);
            }

            AuditLog auditLog = new AuditLog(
                entityName,
                entityId,
                "CREATE_OR_UPDATE",
                null,
                dadosNovos,
                requestContextService.getUsername(),
                requestContextService.getClientIp(),
                requestContextService.getUserAgent()
            );
            auditLog.setUri(requestContextService.getUri());
            auditLog.setMetodoHttp(requestContextService.getMethod());

            auditRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Error intercepting save: ", e);
        }
    }

    @AfterReturning(pointcut = "execution(* org.springframework.data.jpa.repository.JpaRepository+.delete(..))")
    public void interceptDelete(JoinPoint joinPoint) {
        Object entity = joinPoint.getArgs()[0];
        if (entity instanceof AuditLog) {
            return;
        }

        try {
            String entityName;
            String entityId;
            String dadosAnteriores;

            if (entity instanceof Auditable auditable) {
                entityName = auditable.getAuditableEntityName();
                entityId = auditable.getAuditableId() != null
                    ? auditable.getAuditableId().toString() : "unknown";
                dadosAnteriores = objectMapper.writeValueAsString(auditable.getAuditableData());
            } else {
                entityName = entity.getClass().getSimpleName();
                entityId = extractIdFallback(entity);
                dadosAnteriores = serializeToJsonFallback(entity);
            }

            AuditLog auditLog = new AuditLog(
                entityName,
                entityId,
                "DELETE",
                dadosAnteriores,
                null,
                requestContextService.getUsername(),
                requestContextService.getClientIp(),
                requestContextService.getUserAgent()
            );
            auditLog.setUri(requestContextService.getUri());
            auditLog.setMetodoHttp(requestContextService.getMethod());

            auditRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Error intercepting delete: ", e);
        }
    }

    private String extractIdFallback(Object entity) {
        try {
            var idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object id = idField.get(entity);
            return id != null ? id.toString() : "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }

    private String serializeToJsonFallback(Object entity) {
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (Exception e) {
            return "{}";
        }
    }
}
