package ExpooCode.ExpooCode.persistence.dao;

import ExpooCode.ExpooCode.persistence.entity.Suscripcion;
import ExpooCode.ExpooCode.persistence.repository.SuscripcionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SuscripcionDao {

    private final SuscripcionRepository repository;

    public SuscripcionDao(SuscripcionRepository repository) {
        this.repository = repository;
    }

    public List<Suscripcion> findAll() {
        return repository.findAll();
    }

    public Optional<Suscripcion> findById(Long id) {
        return repository.findById(id);
    }

    public Suscripcion save(Suscripcion suscripcion) {
        return repository.save(suscripcion);
    }

    public void delete(Suscripcion suscripcion) {
        repository.delete(suscripcion);
    }
}
