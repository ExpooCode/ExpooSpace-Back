package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.service.UsuarioService;
import ExpooCode.ExpooCode.persistence.entity.Reserva;
import ExpooCode.ExpooCode.persistence.entity.Usuario;
import ExpooCode.ExpooCode.persistence.enums.EstadoUsuario;
import ExpooCode.ExpooCode.persistence.enums.RolUsuario;
import ExpooCode.ExpooCode.persistence.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional
    public Usuario createUsuario(String nombre, String email, String password) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setEstado(EstadoUsuario.Activo);
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public Usuario updateUsuario(Long id, String nombre, String email, String password) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (nombre != null) usuario.setNombre(nombre);
        if (email != null) usuario.setEmail(email);
        if (password != null) usuario.setPassword(password);

        return usuarioRepository.save(usuario);
    }


    @Override
    @Transactional
    public void deleteUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuarioRepository.delete(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean login(String email, String password) {
        return usuarioRepository.findByEmail(email)
                .map(u -> u.getPassword() != null && u.getPassword().equals(password))
                .orElse(false);
    }

    @Override
    @Transactional
    public void logout() {
        log.debug("logout invoked (no-op).");
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getReservasDeUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (usuario.getReservas() == null) {
            return new ArrayList<>();
        }

        return usuario.getReservas()
                .stream()
                .map(reserva -> {
                    try {
                        return "Reserva id: " + reserva.getIdReserva();
                    } catch (Exception e) {
                        return reserva.toString();
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getSuscripcionesDeUsuario(Long id) {
        usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public Usuario cambiarEstadoUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (usuario.getEstado() == null) {
            throw new IllegalStateException("El usuario no tiene estado definido.");
        }

        switch (usuario.getEstado()) {
            case Activo:
                usuario.setEstado(EstadoUsuario.Inactivo);
                break;
            case Inactivo:
                usuario.setEstado(EstadoUsuario.Activo);
                break;
            case Bloqueado:
                throw new IllegalStateException("No se puede cambiar el estado de un usuario bloqueado.");
            case Disponible:
                usuario.setEstado(EstadoUsuario.Activo);
                break;
            default:
                throw new IllegalStateException("Estado no manejado: " + usuario.getEstado());
        }

        return usuarioRepository.save(usuario);
    }
}
