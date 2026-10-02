package br.edu.fiap.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Representa a pessoa cadastrada no marketplace.
 *
 * <p>O mapeamento JPA e os construtores já estão prontos. Os comportamentos
 * permanecem como exercício e devem proteger o estado da entidade.</p>
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(nullable = false, length = 100)
    private String senha;

    @Column(nullable = false)
    private boolean ativo;

    protected Usuario() {
    }

    public Usuario(String nome, String email, String senhaHash) {
        this.nome = nome;
        this.email = normalizarEmail(email);
        atualizarSenhaHash(senhaHash);
        this.ativo = true;
    }

    /** Substitui o hash já codificado da senha. */
    public void atualizarSenhaHash(String novoHash) {
        if (novoHash == null || novoHash.isBlank()) {
            throw new IllegalArgumentException("O hash da senha é obrigatório.");
        }
        this.senha = novoHash;
    }

    /** Permite novamente autenticação e compras. */
    public void ativar() {
        this.ativo = true;
    }

    /** Impede autenticação e novas compras. */
    public void desativar() {
        this.ativo = false;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public boolean isAtivo() { return ativo; }

    private static String normalizarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail é obrigatório.");
        }
        return email.trim().toLowerCase(java.util.Locale.ROOT);
    }
}
