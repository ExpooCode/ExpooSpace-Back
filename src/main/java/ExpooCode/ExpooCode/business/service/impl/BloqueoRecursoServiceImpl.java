package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.BloqueoRecursoDTO;
import ExpooCode.ExpooCode.business.service.BloqueoRecursoService;
import ExpooCode.ExpooCode.persistence.entity.BloqueoRecurso;
import ExpooCode.ExpooCode.persistence.entity.Recurso;
import ExpooCode.ExpooCode.persistence.enums.MotivoBloqueo;
import ExpooCode.ExpooCode.persistence.repository.BloqueoRecursoRepository;
import ExpooCode.ExpooCode.persistence.repository.RecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BloqueoRecursoServiceImpl implements BloqueoRecursoService {

    @Autowired
    private BloqueoRecursoRepository bloqueoRecursoRepository;

    @Autowired
    private RecursoRepository recursoRepository;

    @Override
    public List<BloqueoRecursoDTO> listarBloqueos() {
        return bloqueoRecursoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public BloqueoRecursoDTO obtenerPorId(Long id) {
        return bloqueoRecursoRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Bloqueo no encontrado con ID: " + id));
    }

    @Override
    public BloqueoRecursoDTO crearBloqueo(BloqueoRecursoDTO dto) {
        Recurso recurso = recursoRepository.findById(dto.getIdRecurso().longValue())
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado con ID: " + dto.getIdRecurso()));

        BloqueoRecurso bloqueo = new BloqueoRecurso();
        bloqueo.setRecurso(recurso);
        bloqueo.setFechaInicio(dto.getFechaInicio());
        bloqueo.setFechaFin(dto.getFechaFin());
        bloqueo.setMotivo(MotivoBloqueo.valueOf(dto.getMotivo()));
        bloqueo.setDescripcion(dto.getDescripcion());
        bloqueo.setActivo(dto.isActivo());

        BloqueoRecurso guardado = bloqueoRecursoRepository.save(bloqueo);
        return toDTO(guardado);
    }

    @Override
    public void eliminarBloqueo(Long id) {
        if (!bloqueoRecursoRepository.existsById(id)) {
            throw new RuntimeException("No existe un bloqueo con ID: " + id);
        }
        bloqueoRecursoRepository.deleteById(id);
    }

    private BloqueoRecursoDTO toDTO(BloqueoRecurso bloqueo) {
        return new BloqueoRecursoDTO(
                bloqueo.getIdBloqueo(),
                bloqueo.getRecurso().getIdRecurso().intValue(),
                bloqueo.getFechaInicio(),
                bloqueo.getFechaFin(),
                bloqueo.getMotivo().name(),
                bloqueo.getDescripcion(),
                bloqueo.isActivo()
        );
    }
}
