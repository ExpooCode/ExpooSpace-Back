package ExpooCode.ExpooCode.persistence.repository;

import ExpooCode.ExpooCode.persistence.entity.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long> {

}
