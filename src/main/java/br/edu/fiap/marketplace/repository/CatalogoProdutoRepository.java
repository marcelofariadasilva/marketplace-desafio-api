package br.edu.fiap.marketplace.repository;

import br.edu.fiap.marketplace.entity.CatalogoProduto;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositório JPA do catálogo. */
public interface CatalogoProdutoRepository extends JpaRepository<CatalogoProduto, Long> {
}
