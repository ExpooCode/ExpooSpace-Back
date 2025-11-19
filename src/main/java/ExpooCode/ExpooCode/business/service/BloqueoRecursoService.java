package ExpooCode.ExpooCode.business.service;

import ExpooCode.ExpooCode.business.DTO.BloqueoRecursoDTO;
import java.util.List;

public interface BloqueoRecursoService {
    List<BloqueoRecursoDTO> listarBloqueos();
    BloqueoRecursoDTO obtenerPorId(Long id);
    BloqueoRecursoDTO crearBloqueo(BloqueoRecursoDTO dto);
    void eliminarBloqueo(Long id);
}
