package ExpooCode.ExpooCode.persistence.dao;

import ExpooCode.ExpooCode.persistence.entity.Factura;
import ExpooCode.ExpooCode.persistence.repository.FacturaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FacturaDao {
    private final FacturaRepository facturaRepository;

    public FacturaDao(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    public List<Factura> findAll() {
        return facturaRepository.findAll();
    }

    public Optional<Factura> findById(Long id) {
        return facturaRepository.findById(id);
    }

    public Factura save(Factura factura) {
        return facturaRepository.save(factura);
    }

    public void delete(Factura factura) {
        facturaRepository.delete(factura);
    }

    public boolean existsById(Long id) {
        return facturaRepository.existsById(id);
    }

    public List<Factura> findByUsuarioId(Long idUsuario) {
        return facturaRepository.findByPago_Reserva_Usuario_IdUsuario(idUsuario);
    }
}
