package br.edu.fiap.marketplace.repository;

import br.edu.fiap.marketplace.entity.ConfirmacaoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/** Consultas que asseguram a unicidade da confirmação. */
public interface ConfirmacaoPagamentoRepository
        extends JpaRepository<ConfirmacaoPagamento, Long> {
    Optional<ConfirmacaoPagamento> findByCarrinhoId(Long carrinhoId);
    boolean existsByIdPagamento(String idPagamento);
}
