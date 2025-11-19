package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.ExtraDTO;
import ExpooCode.ExpooCode.business.service.ExtraService;
import ExpooCode.ExpooCode.persistence.dao.ExtraDao;
import ExpooCode.ExpooCode.persistence.entity.Extra;
import ExpooCode.ExpooCode.persistence.mapper.ExtraMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExtraServiceImpl implements ExtraService {

    private final ExtraDao extraDao;
    private final ExtraMapper extraMapper;

    public ExtraServiceImpl(ExtraDao extraDao, ExtraMapper extraMapper) {
        this.extraDao = extraDao;
        this.extraMapper = extraMapper;
    }

    @Override
    @Transactional
    public ExtraDTO crearExtra(ExtraDTO extraDTO) {
        Extra entidad = extraMapper.toEntity(extraDTO);
        // id es autogenerado por BD; relaciones (reservas) se manejan aparte
        Extra saved = extraDao.save(entidad);
        return extraMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExtraDTO> listarExtras() {
        return extraDao.findAll()
                .stream()
                .map(extraMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ExtraDTO obtenerExtraPorId(Integer idExtra) {
        Extra extra = extraDao.findById(idExtra)
                .orElseThrow(() -> new RuntimeException("Extra no encontrado con ID: " + idExtra));
        return extraMapper.toDTO(extra);
    }

    @Override
    @Transactional
    public ExtraDTO actualizarExtra(Integer idExtra, ExtraDTO extraDTO) {
        Extra existente = extraDao.findById(idExtra)
                .orElseThrow(() -> new RuntimeException("Extra no encontrado con ID: " + idExtra));

        // Actualización parcial: mapstruct update (null ignorable) si definiste el método
        extraMapper.updateEntityFromDTO(extraDTO, existente);
        Extra updated = extraDao.save(existente);
        return extraMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void eliminarExtra(Integer idExtra) {
        Extra extra = extraDao.findById(idExtra)
                .orElseThrow(() -> new RuntimeException("Extra no encontrado con ID: " + idExtra));
        extraDao.delete(extra);
    }
}
