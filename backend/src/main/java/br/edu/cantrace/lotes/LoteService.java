package br.edu.cantrace.lotes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoteService {

    private final LoteRepository loteRepository;

    public LoteService(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    public Lote criar(LoteRequest request) {
        loteRepository.findByCodigo(request.codigo())
            .ifPresent(lote -> {
                throw new IllegalArgumentException("Codigo ja existente: " + request.codigo());
            });

        if (request.dataValidade().isBefore(request.dataEntrada())) {
            throw new IllegalArgumentException("Data de validade deve ser posterior a data de entrada");
        }

        Lote lote = new Lote(
            request.codigo(),
            request.nomeProduto(),
            request.dataEntrada(),
            request.dataValidade(),
            request.quantidadeInicial(),
            request.responsavel(),
            request.notas()
        );

        return loteRepository.save(lote);
    }

    @Transactional(readOnly = true)
    public List<Lote> listar() {
        return loteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Lote buscarPorId(UUID id) {
        return loteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Lote nao encontrado: " + id));
    }

    public Lote atualizar(UUID id, LoteRequest request) {
        Lote lote = buscarPorId(id);

        loteRepository.findByCodigo(request.codigo())
            .filter(existing -> !existing.getId().equals(id))
            .ifPresent(existing -> {
                throw new IllegalArgumentException("Codigo ja existente: " + request.codigo());
            });

        if (request.dataValidade().isBefore(request.dataEntrada())) {
            throw new IllegalArgumentException("Data de validade deve ser posterior a data de entrada");
        }

        lote.setCodigo(request.codigo());
        lote.setNomeProduto(request.nomeProduto());
        lote.setDataEntrada(request.dataEntrada());
        lote.setDataValidade(request.dataValidade());
        lote.setQuantidadeInicial(request.quantidadeInicial());
        lote.setResponsavel(request.responsavel());
        lote.setNotas(request.notas());
        lote.setAtualizadoEm(LocalDateTime.now());

        return loteRepository.save(lote);
    }

    public void remover(UUID id) {
        Lote lote = buscarPorId(id);
        lote.softDelete();
        loteRepository.save(lote);
    }
}
