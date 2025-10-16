package ExpooCode.ExpooCode.persistence.repository;

import ExpooCode.ExpooCode.persistence.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByRecurso_IdRecurso(Long recursoId);


    List<Reserva> findByFechaInicioBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByRecurso_IdRecursoAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(Integer idRecurso, LocalDateTime fechaFin, LocalDateTime fechaInicio);

    boolean existsByRecurso_IdRecursoAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqualAndIdReservaNot(Integer idRecurso, LocalDateTime fechaFin, LocalDateTime fechaInicio, Long id);

    List<Reserva> findByUsuario_IdUsuario(Long idUsuario);
}
