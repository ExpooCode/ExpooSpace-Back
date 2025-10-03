package ExpooCode.ExpooCode.persistence.repository;

import ExpooCode.ExpooCode.persistence.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

}