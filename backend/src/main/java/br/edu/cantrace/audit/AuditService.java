package br.edu.cantrace.audit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditRepository auditRepository;

    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public Optional<AuditLog> findById(UUID id) {
        return auditRepository.findById(id);
    }

    public Page<AuditLog> findAll(AuditFilter filter) {
        Specification<AuditLog> spec = buildSpecification(filter);
        PageRequest pageRequest = PageRequest.of(filter.page(), filter.size(),
            Sort.by(Sort.Direction.DESC, "timestamp"));
        return auditRepository.findAll(spec, pageRequest);
    }

    public List<AuditLog> findByEntidade(String entidade) {
        return auditRepository.findByEntidade(entidade);
    }

    public AuditStats getEstatisticas() {
        long total = auditRepository.count();
        Map<String, Long> porEntidade = auditRepository.countByEntidade();
        Map<String, Long> porAcao = auditRepository.countByAcao();
        return new AuditStats(total, porEntidade, porAcao);
    }

    private Specification<AuditLog> buildSpecification(AuditFilter filter) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<jakarta.persistence.criteria.Predicate>();

            if (filter.entidade() != null && !filter.entidade().isBlank()) {
                predicates.add(cb.equal(root.get("entidade"), filter.entidade()));
            }
            if (filter.acao() != null && !filter.acao().isBlank()) {
                predicates.add(cb.equal(root.get("acao"), filter.acao()));
            }
            if (filter.usuario() != null && !filter.usuario().isBlank()) {
                predicates.add(cb.equal(root.get("usuario"), filter.usuario()));
            }
            if (filter.dataInicio() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), filter.dataInicio()));
            }
            if (filter.dataFim() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), filter.dataFim()));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
