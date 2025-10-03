package ExpooCode.ExpooCode.business.service;

import ExpooCode.ExpooCode.persistence.entity.Reserva;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaService {

    /**
     * Crear una nueva reserva
     * @param reserva Entidad con los datos de la reserva a crear
     * @return Reserva creada con ID generado
     * @throws IllegalArgumentException Si los datos no son válidos
     * @throws RuntimeException Si el recurso no está disponible
     */
    Reserva createReserva(Reserva reserva);

    /**
     * Buscar una reserva por ID
     *
     * @param id ID de la reserva
     * @return Reserva encontrada
     * @throws RuntimeException Si la reserva no existe
     */
    Reserva getReservaById(Long id);

    /**
     * Listar todas las reservas
     *
     * @return Lista completa de reservas
     */
    List<Reserva> getAllReservas();

    /**
     * Actualizar una reserva existente
     *
     * RESTRICCIONES:
     * - No se puede cambiar el recurso una vez creada
     * - Validar nuevas fechas y disponibilidad
     *
     * @param id ID de la reserva a actualizar
     * @param reserva Datos actualizados
     * @return Reserva actualizada
     * @throws RuntimeException Si la reserva no existe
     */
    Reserva updateReserva(Long id, Reserva reserva);

    /**
     * Eliminar una reserva
     *
     * @param id ID de la reserva a eliminar
     * @throws RuntimeException Si la reserva no existe
     */
    void deleteReserva(Long id);

    /**
     * Buscar reservas por recurso
     *
     * @param recursoId ID del recurso
     * @return Lista de reservas asociadas al recurso
     */
    List<Reserva> getReservasByRecurso(Long recursoId);

    /**
     * Buscar reservas en un rango de fechas
     *
     * @param start Fecha de inicio
     * @param end   Fecha de fin
     * @return Lista de reservas en el rango
     */
    List<Reserva> getReservasByDateRange(LocalDateTime start, LocalDateTime end);

}
