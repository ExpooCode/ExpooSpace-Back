package ExpooCode.ExpooCode.presentation.Controller;

import ExpooCode.ExpooCode.business.DTO.UsuarioDTO;
import ExpooCode.ExpooCode.business.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expooSpace/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo usuario")
    public ResponseEntity<UsuarioDTO> registrarUsuario(
            @RequestBody UsuarioDTO usuarioDTO
    ) {
        UsuarioDTO nuevoUsuario = usuarioService.createUsuario(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioDTO usuarioDTO
    ) {
        UsuarioDTO usuarioActualizado = usuarioService.updateUsuario(id, usuarioDTO);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión de usuario")
    public ResponseEntity<String> login(@RequestBody UsuarioDTO usuarioDTO) {
        boolean logged = usuarioService.login(usuarioDTO.getEmail(), usuarioDTO.getPassword());
        if (logged) {
            return ResponseEntity.ok("Usuario logueado: " + usuarioDTO.getEmail());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión de usuario")
    public ResponseEntity<String> logout() {
        usuarioService.logout();
        return ResponseEntity.ok("Sesión cerrada correctamente");
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar el estado del usuario")
    public ResponseEntity<UsuarioDTO> cambiarEstado(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.cambiarEstadoUsuario(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/{id}/reservas")
    @Operation(summary = "Obtener reservas de un usuario")
    public ResponseEntity<List<String>> obtenerReservasDeUsuario(@PathVariable Long id) {
        List<String> reservas = usuarioService.getReservasDeUsuario(id);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}/suscripciones")
    @Operation(summary = "Obtener suscripciones de un usuario")
    public ResponseEntity<List<String>> obtenerSuscripcionesDeUsuario(@PathVariable Long id) {
        List<String> suscripciones = usuarioService.getSuscripcionesDeUsuario(id);
        return ResponseEntity.ok(suscripciones);
    }
}