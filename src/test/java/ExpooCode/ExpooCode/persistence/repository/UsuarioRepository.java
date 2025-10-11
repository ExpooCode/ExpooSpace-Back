package ExpooCode.ExpooCode.persistence.repository;

import ExpooCode.ExpooCode.persistence.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuario por email
    Optional<Usuario> findByEmail(String email);

    // Validar existencia de un usuario por email
    boolean existsByEmail(String email);
}
