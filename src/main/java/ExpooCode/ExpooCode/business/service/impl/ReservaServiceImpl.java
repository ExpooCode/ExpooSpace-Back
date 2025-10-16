package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.ReservaDTO;
import ExpooCode.ExpooCode.business.service.ReservaService;
import ExpooCode.ExpooCode.persistence.dao.ExtraDao;
import ExpooCode.ExpooCode.persistence.dao.RecursoDao;
import ExpooCode.ExpooCode.persistence.dao.ReservaDao;
import ExpooCode.ExpooCode.persistence.dao.UsuarioDao;
import ExpooCode.ExpooCode.persistence.entity.Extra;
import ExpooCode.ExpooCode.persistence.entity.Recurso;
import ExpooCode.ExpooCode.persistence.entity.Reserva;
import ExpooCode.ExpooCode.persistence.entity.Usuario;
import ExpooCode.ExpooCode.persistence.enums.EstadoReserva;
import ExpooCode.ExpooCode.persistence.mapper.ReservaMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ReservaServiceImpl implements ReservaService {

    private final ReservaDao reservaDao;
    private final UsuarioDao usuarioDao;
    private final RecursoDao recursoDao;
    private final ExtraDao extraDao;
    private final ReservaMapper reservaMapper;

    public ReservaServiceImpl(ReservaDao reservaDao,
                              UsuarioDao usuarioDao,
                              RecursoDao recursoDao,
                              ExtraDao extraDao, //TODO: Crear flujo EXTRA PARA usar aca el DAO
                              ReservaMapper reservaMapper) {
        this.reservaDao = reservaDao;
        this.usuarioDao = usuarioDao;
        this.recursoDao = recursoDao;
        this.extraDao = extraDao;
        this.reservaMapper = reservaMapper;
    }

    @Override
    @Transactional
    public List<ReservaDTO> getAllReservas() {
        return reservaMapper.toDTOList(reservaDao.findAll());
    }

    @Override
    public ReservaDTO getReservaById(Long id) {
        if (id == null || id <= 0) {
            throw new RuntimeException("Error interno: ID inválido");
        }

        Reserva reserva = reservaDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));

        return reservaMapper.toDTO(reserva);
    }


    @Override
    @Transactional
    public List<ReservaDTO> getReservasByUsuario(Long idUsuario) {
        return reservaMapper.toDTOList(reservaDao.findByUsuarioId(idUsuario));
    }

    @Override
    @Transactional
    public ReservaDTO createReserva(ReservaDTO reservaDTO) {
        Reserva reserva = reservaMapper.toEntity(reservaDTO);
        Usuario usuario = usuarioDao.findById(reservaDTO.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + reservaDTO.getUsuarioId()));

        reserva.setUsuario(usuario);
        if (reservaDTO.getExtraId() != null) {
            Extra extra = extraDao.findById(reservaDTO.getExtraId())
                    .orElseThrow(() -> new EntityNotFoundException("Extra no encontrado con id: " + reservaDTO.getExtraId()));
            reserva.setExtra(extra);
            log.info("Extra encontrado: {}", extra.getNombre());
        } else {
            log.info("No se incluyó extra en la reserva");
        }
        Recurso recurso = recursoDao.findById(reservaDTO.getRecursoId())
                .orElseThrow(() -> new EntityNotFoundException("Recurso no encontrado con id: " + reservaDTO.getRecursoId()));
        reserva.setRecurso(recurso);
        log.info("Recurso encontrado: {}", recurso.getNombre());
        reserva.setFechaInicio(LocalDateTime.now());
        reserva.setEstado(EstadoReserva.Pendiente);
        log.info("Guardando reserva...");
        Reserva saved = reservaDao.save(reserva);
        log.info("Reserva creada exitosamente con ID: {}", saved.getIdReserva());
        return reservaMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public ReservaDTO updateReserva(Long id, ReservaDTO reservaDTO) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error interno: ID inválido");
        }

        Reserva reserva = reservaDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));

        if (reservaDTO.getFechaInicio() != null) {
            reserva.setFechaInicio(reservaDTO.getFechaInicio());
        }
        if (reservaDTO.getEstado() != null) {
            reserva.setEstado(reservaDTO.getEstado());
        }
        if (reservaDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioDao.findById(reservaDTO.getUsuarioId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Usuario no encontrado con id: " + reservaDTO.getUsuarioId()));
            reserva.setUsuario(usuario);
        }

        Reserva updated = reservaDao.save(reserva);
        return reservaMapper.toDTO(updated);
    }


    @Override
    @Transactional
    public boolean deleteReserva(Long id) {
        return reservaDao.findById(id).map(reserva -> {
            reservaDao.delete(reserva);
            return true;
        }).orElse(false);
    }
}
