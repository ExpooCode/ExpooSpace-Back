package ExpooCode.ExpooCode.persistence.dao;

import ExpooCode.ExpooCode.persistence.entity.BloqueoRecurso;
import ExpooCode.ExpooCode.persistence.repository.BloqueoRecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BloqueoRecursoDao {

    @Autowired
    private BloqueoRecursoRepository bloqueoRecursoRepository;

    //  Listar todos los bloqueos
    public List<BloqueoRecurso> findAll() {
        return bloqueoRecursoRepository.findAll();
    }

    //  Buscar bloqueo por ID
    public Optional<BloqueoRecurso> findById(Long id) {
        return bloqueoRecursoRepository.findById(id);
    }

    //  Guardar o actualizar un bloqueo
    public BloqueoRecurso save(BloqueoRecurso bloqueo) {
        return bloqueoRecursoRepository.save(bloqueo);
    }

    //  Eliminar un bloqueo por ID
    public void deleteById(Long id) {
        bloqueoRecursoRepository.deleteById(id);
    }

    //  Verificar si un bloqueo existe
    public boolean existsById(Long id) {
        return bloqueoRecursoRepository.existsById(id);
    }
}
