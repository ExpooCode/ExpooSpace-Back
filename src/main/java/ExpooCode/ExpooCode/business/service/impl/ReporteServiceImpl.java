package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.ReporteDTO;
import ExpooCode.ExpooCode.business.service.ReporteService;
import ExpooCode.ExpooCode.persistence.dao.ReporteDao;
import ExpooCode.ExpooCode.persistence.dao.UsuarioDao;
import ExpooCode.ExpooCode.persistence.entity.Reporte;
import ExpooCode.ExpooCode.persistence.entity.Usuario;
import ExpooCode.ExpooCode.persistence.enums.TipoReporte;
import ExpooCode.ExpooCode.persistence.mapper.ReporteMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final ReporteDao reporteDao;
    private final ReporteMapper reporteMapper;
    private final UsuarioDao usuarioDao;

    public ReporteServiceImpl(ReporteDao reporteDao, ReporteMapper reporteMapper, UsuarioDao usuarioDao) {
        this.reporteDao = reporteDao;
        this.reporteMapper = reporteMapper;
        this.usuarioDao = usuarioDao;
    }

    @Override
    public ReporteDTO generarReporte(ReporteDTO dto) {
        // Mapear el DTO a entidad
        Reporte entity = reporteMapper.toEntity(dto);

        // Buscar y setear el usuario a partir del idUsuario del DTO
        Usuario usuario = usuarioDao.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + dto.getIdUsuario()));

        entity.setUsuario(usuario); //  aquí se asocia el usuario

        // Fecha siempre actual
        entity.setFechaGeneracion(LocalDateTime.now());

        // Asegurar que el contenido no sea null (evitar error de constraint en DB)
        if (entity.getContenido() == null || entity.getContenido().isEmpty()) {
            entity.setContenido("Reporte de tipo " + dto.getTipo() + " generado automáticamente");
        }

        // Guardar y devolver como DTO
        return reporteMapper.toDTO(reporteDao.save(entity));
    }


    @Override
    public List<ReporteDTO> listarReportes() {
        return reporteDao.findAll().stream()
                .map(reporteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReporteDTO obtenerReporte(Long id) {
        Optional<Reporte> reporte = reporteDao.findById(id);
        return reporte.map(reporteMapper::toDTO).orElse(null);
    }

    @Override
    public boolean eliminarReporte(Long id) {
        Optional<Reporte> reporte = reporteDao.findById(id);
        if (reporte.isPresent()) {
            reporteDao.delete(reporte.get());
            return true;
        }
        return false;
    }

    @Override
    public ReporteDTO generarReporteOcupacion() {
        Reporte reporte = new Reporte();
        reporte.setTipo(TipoReporte.Ocupacion);
        reporte.setContenido("Reporte de ocupación generado automáticamente");
        reporte.setFechaGeneracion(LocalDateTime.now());
        return reporteMapper.toDTO(reporteDao.save(reporte));
    }

    @Override
    public ReporteDTO generarReporteIngresos() {
        Reporte reporte = new Reporte();
        reporte.setTipo(TipoReporte.Ingresos);
        reporte.setContenido("Reporte de ingresos generado automáticamente");
        reporte.setFechaGeneracion(LocalDateTime.now());
        return reporteMapper.toDTO(reporteDao.save(reporte));
    }
}
