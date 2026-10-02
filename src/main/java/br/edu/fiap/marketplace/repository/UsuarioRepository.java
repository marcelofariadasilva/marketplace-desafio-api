package br.edu.fiap.marketplace.repository;

import br.edu.fiap.marketplace.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/** Consultas derivadas usadas no cadastro e na autenticação. */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
