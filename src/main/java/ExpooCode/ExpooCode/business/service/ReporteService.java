package ExpooCode.ExpooCode.business.service;

import ExpooCode.ExpooCode.business.DTO.ReporteDTO;

import java.util.List;

public interface ReporteService {
    ReporteDTO generarReporte(ReporteDTO dto);
    List<ReporteDTO> listarReportes();
    ReporteDTO obtenerReporte(Long id);
    boolean eliminarReporte(Long id);

    // extra: reportes específicos
    ReporteDTO generarReporteOcupacion();
    ReporteDTO generarReporteIngresos();
}
