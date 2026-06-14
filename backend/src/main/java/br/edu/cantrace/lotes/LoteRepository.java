package br.edu.cantrace.lotes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoteRepository extends JpaRepository<Lote, UUID> {

    Optional<Lote> findByCodigo(String codigo);

    List<Lote> findByStatus(LoteStatus status);

    List<Lote> findByNomeProdutoContainingIgnoreCase(String nomeProduto);
}
