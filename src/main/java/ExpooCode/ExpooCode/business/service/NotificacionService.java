package ExpooCode.ExpooCode.business.service;

import ExpooCode.ExpooCode.business.DTO.NotificacionDTO;

import java.util.List;

public interface NotificacionService {
    NotificacionDTO crearNotificacion(NotificacionDTO dto);
    NotificacionDTO obtenerNotificacion(Long id);
    List<NotificacionDTO> listarNotificaciones();
    NotificacionDTO marcarLeida(Long id);
    boolean eliminarNotificacion(Long id);
    long contarNoLeidas();
    void marcarTodasLeidas();

}
