package br.edu.cantrace.telemetria;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import br.edu.cantrace.dispositivos.DispositivoIoT;
import br.edu.cantrace.dispositivos.DispositivoRepository;

@DataJpaTest
@Testcontainers
@Disabled("Requires Docker daemon - run outside Maven Docker container")
class TelemetriaRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
        .withDatabaseName("cantrace")
        .withUsername("cantrace")
        .withPassword("cantrace");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TelemetriaRepository telemetriaRepository;

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Test
    void salvar_E_BuscarLeitura() {
        DispositivoIoT dispositivo = new DispositivoIoT("test-01", "Test", "area-01", null);
        dispositivo = dispositivoRepository.save(dispositivo);

        LeituraAmbiental leitura = new LeituraAmbiental(
            dispositivo.getId(), 25.0, 60.0, 500.0, OrigemLeitura.MANUAL, Instant.now()
        );
        leitura = telemetriaRepository.save(leitura);

        Optional<LeituraAmbiental> found = telemetriaRepository.findById(leitura.getId());
        assertTrue(found.isPresent());
        assertEquals(25.0, found.get().getTemperatura());
    }

    @Test
    void findFirstByDispositivoIdOrderByTimestampDesc_DeveRetornarUltima() {
        DispositivoIoT dispositivo = new DispositivoIoT("test-02", "Test", "area-01", null);
        dispositivo = dispositivoRepository.save(dispositivo);

        Instant now = Instant.now();
        telemetriaRepository.save(new LeituraAmbiental(dispositivo.getId(), 20.0, 50.0, 400.0, OrigemLeitura.MANUAL, now.minusSeconds(3600)));
        telemetriaRepository.save(new LeituraAmbiental(dispositivo.getId(), 25.0, 60.0, 500.0, OrigemLeitura.MANUAL, now));

        Optional<LeituraAmbiental> ultima = telemetriaRepository.findFirstByDispositivoIdOrderByTimestampDesc(dispositivo.getId());
        assertTrue(ultima.isPresent());
        assertEquals(25.0, ultima.get().getTemperatura());
    }

    @Test
    void findByDispositivoIdOrderByTimestampDesc_DeveRetornarHistorico() {
        DispositivoIoT dispositivo = new DispositivoIoT("test-03", "Test", "area-01", null);
        dispositivo = dispositivoRepository.save(dispositivo);

        Instant now = Instant.now();
        telemetriaRepository.save(new LeituraAmbiental(dispositivo.getId(), 20.0, 50.0, 400.0, OrigemLeitura.MANUAL, now.minusSeconds(7200)));
        telemetriaRepository.save(new LeituraAmbiental(dispositivo.getId(), 22.0, 55.0, 450.0, OrigemLeitura.MANUAL, now.minusSeconds(3600)));
        telemetriaRepository.save(new LeituraAmbiental(dispositivo.getId(), 25.0, 60.0, 500.0, OrigemLeitura.MANUAL, now));

        List<LeituraAmbiental> historico = telemetriaRepository.findByDispositivoIdOrderByTimestampDesc(
            dispositivo.getId(), PageRequest.of(0, 10));
        assertEquals(3, historico.size());
    }
}
