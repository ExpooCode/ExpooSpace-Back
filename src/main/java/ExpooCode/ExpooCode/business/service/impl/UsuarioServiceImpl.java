package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.RegisterRequest;
import ExpooCode.ExpooCode.business.DTO.UsuarioDTO;
import ExpooCode.ExpooCode.business.service.UsuarioService;
import ExpooCode.ExpooCode.persistence.entity.Usuario;
import ExpooCode.ExpooCode.persistence.enums.EstadoUsuario;
import ExpooCode.ExpooCode.persistence.enums.RolUsuario;
import ExpooCode.ExpooCode.persistence.mapper.UsuarioMapper;
import ExpooCode.ExpooCode.persistence.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UsuarioDTO registerPublicUser(RegisterRequest request) {
        log.info("📝 Registro público - nuevo usuario: {}", request.getEmail());


        if (usuarioRepository.existsByEmail(request.getEmail())) {
            log.warn("Email ya registrado: {}", request.getEmail());
            throw new RuntimeException("El email ya está registrado");
        }


        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail().toLowerCase().trim()); // Normalizar email


        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        // Configuración por defecto para registro público
        usuario.setRol(RolUsuario.Visitante); // Rol por defecto
        usuario.setEstado(EstadoUsuario.Activo);

        // Guardar
        Usuario saved = usuarioRepository.save(usuario);

        log.info("✅ Usuario público registrado: {} con rol {}", saved.getEmail(), saved.getRol());

        return usuarioMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioMapper.toDTOList(usuarioRepository.findAll());
    }

    @Override
    @Transactional
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.CONFLICT, // Código 409
                    "Ya existe un usuario con el email: " + usuarioDTO.getEmail()
            );
        }
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuario.setEstado(EstadoUsuario.Activo); // Estado inicial por defecto
        Usuario saved = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO getUsuarioById(Long id) {
        if (id <= 0) {
            throw new RuntimeException("Error interno: ID inválido");
        }
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return usuarioMapper.toDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioDTO updateUsuario(Long id, UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getEmail() != null && !usuarioDTO.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuarioDTO.getNombre() != null) usuario.setNombre(usuarioDTO.getNombre());
        if (usuarioDTO.getEmail() != null) usuario.setEmail(usuarioDTO.getEmail());
        if (usuarioDTO.getPassword() != null) usuario.setPassword(usuarioDTO.getPassword());
        if (usuarioDTO.getRol() != null) usuario.setRol(usuarioDTO.getRol());
        if (usuarioDTO.getEstado() != null) usuario.setEstado(usuarioDTO.getEstado());

        Usuario updated = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(updated);
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
        return new ArrayList<>(); // TODO: implementar cuando tengas suscripciones
    }

    @Override
    @Transactional
    public UsuarioDTO cambiarEstadoUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (usuario.getEstado() == null) {
            throw new IllegalStateException("El usuario no tiene estado definido.");
        }

        switch (usuario.getEstado()) {
            case Activo -> usuario.setEstado(EstadoUsuario.Inactivo);
            case Inactivo, Disponible -> usuario.setEstado(EstadoUsuario.Activo);
            case Bloqueado -> throw new IllegalStateException("No se puede cambiar el estado de un usuario bloqueado.");
            default -> throw new IllegalStateException("Estado no manejado: " + usuario.getEstado());
        }

        Usuario updated = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(updated);
    }
}
