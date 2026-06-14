package br.edu.cantrace.audit;

import java.util.Map;

public interface Auditable {
    Object getAuditableId();
    String getAuditableEntityName();
    Map<String, Object> getAuditableData();
}
