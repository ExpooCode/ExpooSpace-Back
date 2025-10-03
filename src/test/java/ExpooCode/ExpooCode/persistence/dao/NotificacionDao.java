package ExpooCode.ExpooCode.persistence.dao;

import ExpooCode.ExpooCode.persistence.entity.Notificacion;
import ExpooCode.ExpooCode.persistence.repository.NotificacionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class NotificacionDao {

    private final NotificacionRepository repository;

    public NotificacionDao(NotificacionRepository repository) {
        this.repository = repository;
    }

    public List<Notificacion> findAll() {
        return repository.findAll();
    }

    public Optional<Notificacion> findById(Long id) {
        return repository.findById(id);
    }

    public Notificacion save(Notificacion notificacion) {
        return repository.save(notificacion);
    }

    public void saveAll(List<Notificacion> notificaciones) {
        repository.saveAll(notificaciones);
    }

    public void delete(Notificacion notificacion) {
        repository.delete(notificacion);
    }

    public long countByLeidoFalse() {
        return repository.countByLeidoFalse();
    }
}
