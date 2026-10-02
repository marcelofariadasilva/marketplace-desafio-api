package br.edu.fiap.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/** Produto anunciado no catálogo do marketplace. */
@Entity
@Table(name = "catalogo_produtos")
public class CatalogoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String nome;

    @Column(nullable = false, length = 300)
    private String descricao;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer estoque;

    @Column(nullable = false)
    private boolean ativo;

    protected CatalogoProduto() {
    }

    public CatalogoProduto(
            String nome,
            String descricao,
            BigDecimal preco,
            Integer estoque,
            boolean ativo) {
        atualizarDados(nome, descricao, preco, estoque, ativo);
        this.ativo = ativo;
    }

    /** Altera o preço somente para valor positivo. */
    public void alterarPreco(BigDecimal novoPreco) {
        validarPreco(novoPreco);
        this.preco = novoPreco;
    }

    /** Dá baixa em estoque, impedindo saldo negativo. */
    public void baixarEstoque(int quantidade) {
        validarQuantidadePositiva(quantidade);
        if (quantidade > estoque) {
            throw new IllegalStateException("Estoque insuficiente.");
        }
        estoque -= quantidade;
    }

    /** Repõe somente quantidade positiva. */
    public void reporEstoque(int quantidade) {
        validarQuantidadePositiva(quantidade);
        estoque += quantidade;
    }

    /** Disponibiliza o produto para novas compras. */
    public void ativar() {
        this.ativo = true;
    }

    /** Retira o produto de novas compras sem apagar o histórico. */
    public void desativar() {
        this.ativo = false;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getPreco() { return preco; }
    public Integer getEstoque() { return estoque; }
    public boolean isAtivo() { return ativo; }

    /** Atualiza os dados administrativos sem expor setters genéricos. */
    public void atualizarDados(String nome, String descricao, BigDecimal preco, Integer estoque, boolean ativo) {
        if (nome == null || nome.isBlank() || descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Nome e descrição são obrigatórios.");
        }
        validarPreco(preco);
        if (estoque == null || estoque < 0) {
            throw new IllegalArgumentException("Estoque não pode ser negativo.");
        }
        this.nome = nome.trim();
        this.descricao = descricao.trim();
        this.preco = preco;
        this.estoque = estoque;
        this.ativo = ativo;
    }

    private static void validarPreco(BigDecimal preco) {
        if (preco == null || preco.signum() <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero.");
        }
    }

    private static void validarQuantidadePositiva(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva.");
        }
    }
}
