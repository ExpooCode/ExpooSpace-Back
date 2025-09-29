package ExpooCode.ExpooCode.persistence.dao;


import ExpooCode.ExpooCode.persistence.entity.Pago;
import ExpooCode.ExpooCode.persistence.repository.PagoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PagoDao {
    private final PagoRepository pagoRepository;

    public PagoDao(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    public Optional<Pago> findById(Long id) {
        return pagoRepository.findById(id);
    }

    public Pago save(Pago pago) {
        return pagoRepository.save(pago);
    }

    public void delete(Pago pago) {
        pagoRepository.delete(pago);
    }

    public void deleteById(Long id) {
        pagoRepository.deleteById(id);
    }
}
