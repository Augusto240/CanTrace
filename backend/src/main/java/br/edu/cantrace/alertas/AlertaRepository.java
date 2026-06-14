package br.edu.cantrace.alertas;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlertaRepository extends JpaRepository<AlertaAmbiental, UUID>, JpaSpecificationExecutor<AlertaAmbiental> {

    List<AlertaAmbiental> findByStatusOrderByCriadoEmDesc(AlertaStatus status, PageRequest pageRequest);

    Optional<AlertaAmbiental> findByIdAndStatus(UUID id, AlertaStatus status);

    long countByStatus(AlertaStatus status);

    long countByNivel(NivelAlerta nivel);

    List<AlertaAmbiental> findByDispositivoIdOrderByCriadoEmDesc(UUID dispositivoId);

    @Query("SELECT a FROM AlertaAmbiental a WHERE a.dispositivoId = :dispositivoId AND a.status = :status ORDER BY a.criadoEm DESC")
    List<AlertaAmbiental> findByDispositivoIdAndStatus(@Param("dispositivoId") UUID dispositivoId, @Param("status") AlertaStatus status, PageRequest pageRequest);
}
