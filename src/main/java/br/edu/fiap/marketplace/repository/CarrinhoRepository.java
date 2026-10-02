package br.edu.fiap.marketplace.repository;

import br.edu.fiap.marketplace.entity.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/** Consultas de carrinhos por usuário. */
public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    List<Carrinho> findByUsuarioId(Long usuarioId);
}
