package br.edu.cantrace.telemetria;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TelemetriaRepository extends JpaRepository<LeituraAmbiental, UUID>, JpaSpecificationExecutor<LeituraAmbiental> {

    Optional<LeituraAmbiental> findFirstByDispositivoIdOrderByTimestampDesc(UUID dispositivoId);

    List<LeituraAmbiental> findByDispositivoIdOrderByTimestampDesc(UUID dispositivoId, Pageable pageable);

    @Query("SELECT l FROM LeituraAmbiental l WHERE l.dispositivoId = :dispositivoId AND l.timestamp BETWEEN :inicio AND :fim ORDER BY l.timestamp DESC")
    Page<LeituraAmbiental> findByDispositivoIdAndPeriodo(
        @Param("dispositivoId") UUID dispositivoId,
        @Param("inicio") Instant inicio,
        @Param("fim") Instant fim,
        Pageable pageable);
}
