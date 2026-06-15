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

    @Query(value = "SELECT entidade, COUNT(*) as cnt FROM registros_auditoria GROUP BY entidade", nativeQuery = true)
    List<Object[]> countByEntidadeRaw();

    @Query(value = "SELECT acao, COUNT(*) as cnt FROM registros_auditoria GROUP BY acao", nativeQuery = true)
    List<Object[]> countByAcaoRaw();

    long count();
}
