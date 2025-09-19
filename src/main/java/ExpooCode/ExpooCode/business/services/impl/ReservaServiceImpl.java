package ExpooCode.ExpooCode.business.services.impl;

import ExpooCode.ExpooCode.business.services.ReservaService;
import ExpooCode.ExpooCode.persistence.entity.Reserva;
import ExpooCode.ExpooCode.persistence.repository.ReservaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
//@RequiredArgsConstructor
@Slf4j
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaServiceImpl(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Override
    @Transactional
    public Reserva createReserva(Reserva reserva) {
        // Validar fechas
        if (reserva.getFechaInicio() == null || reserva.getFechaFin() == null ||
                reserva.getFechaInicio().isAfter(reserva.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }

        // Validar recurso
        if (reserva.getRecurso() == null || reserva.getUsuario() == null) {
            throw new IllegalArgumentException("El recurso y el usuario son obligatorios.");
        }


        // Validar disponibilidad del RECURSO (excluyendo la misma reserva) TODO: METODO PARA REALIZAR VALIDACION CORRECTA DE LA DISPONIBILIDAD DEL REPOSITORY?
        /*
        boolean ocupado = reservaRepository.existsByRecurso_IdRecursoAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
                reserva.getRecurso().getIdRecurso(),
                reserva.getFechaFin(),
                reserva.getFechaInicio()
        );

        if (ocupado) {
            throw new RuntimeException("El recurso no está disponible en las fechas seleccionadas.");
        } */

        return reservaRepository.save(reserva);
    }

    @Override
    @Transactional(readOnly = true)
    public Reserva getReservaById(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reserva> getAllReservas() {
        return reservaRepository.findAll();
    }

    @Override
    @Transactional
    public Reserva updateReserva(Long id, Reserva reserva) {
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));

        // No se puede cambiar el recurso
        if (!reservaExistente.getRecurso().getIdRecurso().equals(reserva.getRecurso().getIdRecurso())) {
            throw new IllegalArgumentException("No se puede cambiar el recurso de una reserva existente.");
        }

        // Validar fechas
        if (reserva.getFechaInicio() == null || reserva.getFechaFin() == null ||
                reserva.getFechaInicio().isAfter(reserva.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }

        // Validar disponibilidad (excluyendo la misma reserva) TODO: METODO PARA REALIZAR VALIDACION CORRECTA DE LAS DISPONIBILIDAD DEL REPOSITORY?
        /*
        boolean ocupado = reservaRepository.existsByRecurso_IdRecursoAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqualAndIdReservaNot(
                reserva.getRecurso().getIdRecurso(),
                reserva.getFechaFin(),
                reserva.getFechaInicio(),
                id
        );

        if (ocupado) {
            throw new RuntimeException("El recurso no está disponible en las nuevas fechas seleccionadas.");
        }*/

        reservaExistente.setFechaInicio(reserva.getFechaInicio());
        reservaExistente.setFechaFin(reserva.getFechaFin());
        reservaExistente.setEstado(reserva.getEstado());

        return reservaRepository.save(reservaExistente);
    }

    @Override
    @Transactional
    public void deleteReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
        reservaRepository.delete(reserva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reserva> getReservasByRecurso(Long recursoId) {
        return reservaRepository.findByRecurso_IdRecurso(recursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reserva> getReservasByDateRange(LocalDateTime start, LocalDateTime end) {
        return reservaRepository.findByFechaInicioBetween(start, end);
    }
}