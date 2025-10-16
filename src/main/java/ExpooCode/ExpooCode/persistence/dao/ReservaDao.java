package ExpooCode.ExpooCode.persistence.dao;

import ExpooCode.ExpooCode.persistence.entity.Reserva;
import ExpooCode.ExpooCode.persistence.repository.ReservaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ReservaDao {

    private final ReservaRepository reservaRepository;

    public ReservaDao(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> findById(Long id) {
        return reservaRepository.findById(id);
    }

    public Reserva save(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public void delete(Reserva reserva) {
        reservaRepository.delete(reserva);
    }

    public boolean existsById(Long id) {
        return reservaRepository.existsById(id);
    }

    public List<Reserva> findByRecursoId(Long recursoId) {
        return reservaRepository.findByRecurso_IdRecurso(recursoId);
    }

    public List<Reserva> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return reservaRepository.findByFechaInicioBetween(start, end);
    }
    public List<Reserva> findByUsuarioId(Long idUsuario) {
        return reservaRepository.findByUsuario_IdUsuario(idUsuario);
    }
}
