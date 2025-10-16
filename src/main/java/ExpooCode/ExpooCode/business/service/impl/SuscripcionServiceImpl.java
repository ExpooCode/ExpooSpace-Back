package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.SuscripcionDTO;
import ExpooCode.ExpooCode.business.service.SuscripcionService;
import ExpooCode.ExpooCode.persistence.dao.SuscripcionDao;
import ExpooCode.ExpooCode.persistence.dao.UsuarioDao;
import ExpooCode.ExpooCode.persistence.entity.Suscripcion;
import ExpooCode.ExpooCode.persistence.entity.Usuario;
import ExpooCode.ExpooCode.persistence.mapper.SuscripcionMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SuscripcionServiceImpl implements SuscripcionService {

    private final SuscripcionDao suscripcionDao;
    private final UsuarioDao usuarioDao;
    private final SuscripcionMapper mapper;

    public SuscripcionServiceImpl(SuscripcionDao suscripcionDao, UsuarioDao usuarioDao, SuscripcionMapper mapper) {
        this.suscripcionDao = suscripcionDao;
        this.usuarioDao = usuarioDao;
        this.mapper = mapper;
    }

    @Override
    public SuscripcionDTO crearSuscripcion(SuscripcionDTO dto) {
        Optional<Usuario> usuario = usuarioDao.findById(dto.getIdUsuario());
        if (usuario.isEmpty()) {
            throw new RuntimeException("El usuario con ID " + dto.getIdUsuario() + " no existe");
        }

        Suscripcion entity = mapper.toEntity(dto);
        return mapper.toDTO(suscripcionDao.save(entity));
    }

    @Override
    public SuscripcionDTO obtenerSuscripcion(Long id) {
        return suscripcionDao.findById(id)
                .map(mapper::toDTO)
                .orElse(null);
    }

    @Override
    public List<SuscripcionDTO> listarSuscripciones() {
        return suscripcionDao.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SuscripcionDTO actualizarSuscripcion(Long id, SuscripcionDTO dto) {
        Optional<Suscripcion> optional = suscripcionDao.findById(id);
        if (optional.isPresent()) {
            Suscripcion existente = optional.get();
            existente.setTipoPlan(dto.getTipoPlan());
            existente.setHorasIncluidas(dto.getHorasIncluidas());
            existente.setPrecioMensual(dto.getPrecioMensual());
            existente.setFechaInicio(dto.getFechaInicio());
            existente.setFechaFin(dto.getFechaFin());
            existente.setEstado(dto.getEstado());
            existente.setRenovacionAutomatica(dto.isRenovacionAutomatica());
            return mapper.toDTO(suscripcionDao.save(existente));
        }
        return null;
    }

    @Override
    public boolean eliminarSuscripcion(Long id) {
        Optional<Suscripcion> optional = suscripcionDao.findById(id);
        if (optional.isPresent()) {
            suscripcionDao.delete(optional.get());
            return true;
        }
        return false;
    }
}
