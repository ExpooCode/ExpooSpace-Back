package ExpooCode.ExpooCode.persistence.dao;

import ExpooCode.ExpooCode.persistence.entity.Reporte;
import ExpooCode.ExpooCode.persistence.repository.ReporteRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ReporteDao {

    private final ReporteRepository reporteRepository;

    public ReporteDao(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    public List<Reporte> findAll() {
        return reporteRepository.findAll();
    }

    public Optional<Reporte> findById(Long id) {
        return reporteRepository.findById(id);
    }

    public Reporte save(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    public void delete(Reporte reporte) {
        reporteRepository.delete(reporte);
    }
}
