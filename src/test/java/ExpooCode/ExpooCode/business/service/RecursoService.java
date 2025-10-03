package ExpooCode.ExpooCode.business.service;

import ExpooCode.ExpooCode.business.DTO.RecursoDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RecursoService {

    List<RecursoDTO> getAllRecursos();

    RecursoDTO createRecurso(RecursoDTO recursoDTO);

    Optional<RecursoDTO> getRecursoById(Long id);

    Optional<RecursoDTO> updateRecurso(Long id, RecursoDTO recursoDTO);

    boolean deleteRecurso(Long id);

    Set<String> getTiposDeRecursos();
}
