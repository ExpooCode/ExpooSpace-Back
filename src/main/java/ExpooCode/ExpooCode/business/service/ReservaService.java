package ExpooCode.ExpooCode.business.service;

import ExpooCode.ExpooCode.business.DTO.ReservaDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReservaService {

    /**
     * Crear una nueva reserva.
     *
     * @param reservaDTO Datos de la reserva a crear.
     * @return ReservaDTO creada con ID generado.
     * @throws IllegalArgumentException Si los datos no son válidos.
     * @throws RuntimeException Si el recurso no está disponible.
     */
    @Transactional
    ReservaDTO createReserva(ReservaDTO reservaDTO);

    /**
     * Buscar una reserva por ID.
     *
     * @param id ID de la reserva.
     * @return Optional con la reserva encontrada.
     * @throws RuntimeException Si la reserva no existe.
     */
    @Transactional(readOnly = true)
    ReservaDTO getReservaById(Long id);

    /**
     * Listar todas las reservas.
     *
     * @return Lista completa de reservas.
     */
    @Transactional(readOnly = true)
    List<ReservaDTO> getAllReservas();

    /**
     * Buscar reservas por ID de usuario.
     *
     * @param idUsuario ID del usuario.
     * @return Lista de reservas asociadas al usuario.
     */
    @Transactional(readOnly = true)
    List<ReservaDTO> getReservasByUsuario(Long idUsuario);

    /**
     * Actualizar una reserva existente.
     *
     * @param id ID de la reserva a actualizar.
     * @param reservaDTO Datos actualizados.
     * @return Optional con la reserva actualizada.
     * @throws RuntimeException Si la reserva no existe.
     */
    @Transactional
    ReservaDTO updateReserva(Long id, ReservaDTO reservaDTO);

    /**
     * Eliminar una reserva.
     *
     * @param id ID de la reserva a eliminar.
     * @return true si la reserva fue eliminada correctamente.
     * @throws RuntimeException Si la reserva no existe.
     */
    @Transactional
    boolean deleteReserva(Long id);
}
