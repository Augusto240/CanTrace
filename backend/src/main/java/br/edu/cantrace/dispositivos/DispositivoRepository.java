package br.edu.cantrace.dispositivos;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DispositivoRepository extends JpaRepository<DispositivoIoT, UUID>, JpaSpecificationExecutor<DispositivoIoT> {
    Optional<DispositivoIoT> findByDeviceCode(String deviceCode);
    boolean existsByDeviceCode(String deviceCode);
    int countByStatus(DispositivoStatus status);
}
