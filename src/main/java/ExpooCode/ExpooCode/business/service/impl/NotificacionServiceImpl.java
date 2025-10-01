package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.NotificacionDTO;
import ExpooCode.ExpooCode.business.service.NotificacionService;
import ExpooCode.ExpooCode.persistence.dao.NotificacionDao;
import ExpooCode.ExpooCode.persistence.dao.UsuarioDao;
import ExpooCode.ExpooCode.persistence.entity.Notificacion;
import ExpooCode.ExpooCode.persistence.entity.Usuario;
import ExpooCode.ExpooCode.persistence.mapper.NotificacionMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionDao notificacionDao;
    private final NotificacionMapper notificacionMapper;
    private final UsuarioDao usuarioDao;

    public NotificacionServiceImpl(NotificacionDao notificacionDao, NotificacionMapper notificacionMapper, UsuarioDao usuarioDao) {
        this.notificacionDao = notificacionDao;
        this.notificacionMapper = notificacionMapper;
        this.usuarioDao = usuarioDao;
    }

    @Override
    public NotificacionDTO crearNotificacion(NotificacionDTO dto) {
        Notificacion entidad = notificacionMapper.toEntity(dto);

        // Buscar el usuario en BD
        Usuario usuario = usuarioDao.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + dto.getIdUsuario()));

        entidad.setUsuario(usuario);  // asigna el usuario real
        entidad.setLeido(false);      // siempre empieza como no leída
        entidad.setFechaEnvio(LocalDateTime.now()); // poner la fecha

        return notificacionMapper.toDTO(notificacionDao.save(entidad));

    }

    @Override
    public NotificacionDTO obtenerNotificacion(Long id) {
        return notificacionDao.findById(id)
                .map(notificacionMapper::toDTO)
                .orElse(null);
    }

    @Override
    public List<NotificacionDTO> listarNotificaciones() {
        return notificacionDao.findAll()
                .stream()
                .map(notificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificacionDTO marcarLeida(Long id) {
        return notificacionDao.findById(id)
                .map(n -> {
                    n.setLeido(true);
                    return notificacionMapper.toDTO(notificacionDao.save(n));
                })
                .orElse(null);
    }

    @Override
    public boolean eliminarNotificacion(Long id) {
        return notificacionDao.findById(id)
                .map(n -> {
                    notificacionDao.delete(n);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public long contarNoLeidas() {
        return notificacionDao.countByLeidoFalse();
    }

    @Override
    public void marcarTodasLeidas() {
        List<Notificacion> todas = notificacionDao.findAll();
        todas.forEach(n -> n.setLeido(true));
        notificacionDao.saveAll(todas);
    }
}
