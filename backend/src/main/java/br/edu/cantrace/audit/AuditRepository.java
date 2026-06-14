package br.edu.cantrace.audit;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<AuditLog, UUID>, JpaSpecificationExecutor<AuditLog> {

    List<AuditLog> findByEntidadeAndEntidadeId(String entidade, String entidadeId);

    List<AuditLog> findByEntidade(String entidade);

    @Query("SELECT a.entidade, COUNT(a) FROM AuditLog a GROUP BY a.entidade")
    Map<String, Long> countByEntidade();

    @Query("SELECT a.acao, COUNT(a) FROM AuditLog a GROUP BY a.acao")
    Map<String, Long> countByAcao();
}
