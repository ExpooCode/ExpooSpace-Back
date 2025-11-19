package ExpooCode.ExpooCode.business.service;

import ExpooCode.ExpooCode.business.DTO.SuscripcionDTO;
import java.util.List;

public interface SuscripcionService {

    SuscripcionDTO crearSuscripcion(SuscripcionDTO dto);
    SuscripcionDTO obtenerSuscripcion(Long id);
    List<SuscripcionDTO> listarSuscripciones();
    SuscripcionDTO actualizarSuscripcion(Long id, SuscripcionDTO dto);
    boolean eliminarSuscripcion(Long id);
}
