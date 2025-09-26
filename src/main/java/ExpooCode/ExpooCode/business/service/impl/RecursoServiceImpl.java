package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.RecursoDTO;
import ExpooCode.ExpooCode.business.service.RecursoService;
import ExpooCode.ExpooCode.persistence.dao.RecursoDao;
import ExpooCode.ExpooCode.persistence.entity.Recurso;
import ExpooCode.ExpooCode.persistence.mapper.RecursoMapper;
import ExpooCode.ExpooCode.persistence.repository.RecursoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class RecursoServiceImpl implements RecursoService {

    private final RecursoDao recursoDao;
    private final RecursoMapper recursoMapper;

    public RecursoServiceImpl(RecursoDao recursoDao, RecursoMapper recursoMapper) {
        this.recursoDao = recursoDao;
        this.recursoMapper = recursoMapper;
    }

    @Override
    @Transactional
    public List<RecursoDTO> getAllRecursos() {
        return recursoMapper.toDTOList(recursoDao.findAll());
    }

    @Override
    @Transactional
    public RecursoDTO createRecurso(RecursoDTO recursoDTO) {
        Recurso recurso = recursoMapper.toEntity(recursoDTO);
        Recurso saved = recursoDao.save(recurso);
        return recursoMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public Optional<RecursoDTO> getRecursoById(Long id) {
        return recursoDao.findById(id)
                .map(recursoMapper::toDTO);
    }

    @Override
    @Transactional
    public Optional<RecursoDTO> updateRecurso(Long id, RecursoDTO recursoDTO) {
        return recursoDao.findById(id).map(recurso -> {
            if (recursoDTO.getNombre() != null) recurso.setNombre(recursoDTO.getNombre());
            if (recursoDTO.getTipo() != null) recurso.setTipo(recursoDTO.getTipo());
            if (recursoDTO.getCapacidad() != null) recurso.setCapacidad(recursoDTO.getCapacidad());
            if (recursoDTO.getEstadoRecurso() != null) recurso.setEstadoRecurso(recursoDTO.getEstadoRecurso());

            Recurso updated = recursoDao.save(recurso);
            return recursoMapper.toDTO(updated);
        });
    }

    @Override
    @Transactional
    public boolean deleteRecurso(Long id) {
        return recursoDao.findById(id).map(recurso -> {
            recursoDao.delete(recurso);
            return true;
        }).orElse(false);
    }

    @Override
    @Transactional
    public Set<String> getTiposDeRecursos() {
        return recursoDao.findAll()
                .stream()
                .map(r -> r.getTipo().name())
                .collect(Collectors.toSet());
    }
}