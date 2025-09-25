package ExpooCode.ExpooCode.presentation.Controller;

import ExpooCode.ExpooCode.business.service.UsuarioService;
import ExpooCode.ExpooCode.persistence.entity.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Listar todos los usuarios", description = "Obtiene una lista de todos los usuarios registrados (solo Admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (no Admin)"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Usuario> registrarUsuario(
            @Parameter(description = "Nombre del usuario") @RequestParam String nombre,
            @Parameter(description = "Email del usuario") @RequestParam String email,
            @Parameter(description = "Password del usuario") @RequestParam String password
    ) {
        Usuario nuevoUsuario = usuarioService.createUsuario(nombre, email, password);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(
            @Parameter(description = "ID del usuario a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        Usuario usuario = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id,
            @RequestParam String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password
    ) {
        Usuario usuarioActualizado = usuarioService.updateUsuario(id, nombre, email, password);
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
    public ResponseEntity<String> login(
            @RequestParam String email,
            @RequestParam String password
    ) {
        boolean logged = usuarioService.login(email, password);
        if (logged) {
            return ResponseEntity.ok("Usuario logueado: " + email);
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
    public ResponseEntity<Usuario> cambiarEstado(@PathVariable Long id) {
        Usuario usuario = usuarioService.cambiarEstadoUsuario(id);
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