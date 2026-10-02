package br.edu.fiap.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Representa uma escolha simples de produto, quantidade e usuário.
 * Cada registro corresponde a um produto no carrinho do desafio.
 */
@Entity
@Table(name = "carrinhos")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private CatalogoProduto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCarrinho status;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    protected Carrinho() {
    }

    public Carrinho(Usuario usuario, CatalogoProduto produto, Integer quantidade) {
        if (usuario == null || produto == null) {
            throw new IllegalArgumentException("Usuário e produto são obrigatórios.");
        }
        this.usuario = usuario;
        this.produto = produto;
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva.");
        }
        this.quantidade = quantidade;
        this.status = StatusCarrinho.ABERTO;
        this.criadoEm = Instant.now();
    }

    /** Altera a quantidade somente enquanto o carrinho estiver aberto. */
    public void alterarQuantidade(int novaQuantidade) {
        exigirAberto();
        if (novaQuantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva.");
        }
        this.quantidade = novaQuantidade;
    }

    /** Calcula o total com o preço atual do produto. */
    public BigDecimal calcularTotal() {
        return produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
    }

    /** Finaliza um carrinho aberto após aprovação do pagamento. */
    public void finalizar() {
        exigirAberto();
        this.status = StatusCarrinho.FINALIZADO;
    }

    /** Cancela um carrinho aberto. */
    public void cancelar() {
        exigirAberto();
        this.status = StatusCarrinho.CANCELADO;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public CatalogoProduto getProduto() { return produto; }
    public Integer getQuantidade() { return quantidade; }
    public StatusCarrinho getStatus() { return status; }
    public Instant getCriadoEm() { return criadoEm; }

    private void exigirAberto() {
        if (status != StatusCarrinho.ABERTO) {
            throw new IllegalStateException("Somente carrinho aberto pode ser alterado.");
        }
    }
}
