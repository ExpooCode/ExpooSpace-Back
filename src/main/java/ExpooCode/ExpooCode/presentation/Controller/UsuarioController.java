package ExpooCode.ExpooCode.presentation.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/expooSpace/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UsuarioController {

    // GET: Listar todos los usuarios (solo Admin)
    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Obtiene una lista de todos los usuarios registrados (solo Admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (no Admin)"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<String>> listarUsuarios() {
        List<String> usuarios = new ArrayList<>();
        usuarios.add("Usuario 1");
        usuarios.add("Usuario 2");
        return ResponseEntity.ok(usuarios);
    }

    // POST: Crear un nuevo usuario (registro público)
    @PostMapping
    @Operation(summary = "Registrar un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> registrarUsuario(
            @Parameter(description = "Nombre del usuario") @RequestParam String nombre,
            @Parameter(description = "Email del usuario") @RequestParam String email
    ) {
        return ResponseEntity.status(201).body("Usuario registrado: " + nombre + " (" + email + ")");
    }

    // GET: Obtener usuario por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<String> obtenerUsuarioPorId(
            @Parameter(description = "ID del usuario a buscar", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok("Usuario con ID: " + id);
    }

    // PUT: Actualizar usuario
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<String> actualizarUsuario(
            @PathVariable Long id,
            @RequestParam String nombre
    ) {
        return ResponseEntity.ok("Usuario con ID " + id + " actualizado a nombre: " + nombre);
    }

    // DELETE: Eliminar usuario
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }

    // POST: Login
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión de usuario")
    public ResponseEntity<String> login(
            @RequestParam String email,
            @RequestParam String password
    ) {
        return ResponseEntity.ok("Usuario logueado: " + email);
    }

    // POST: Logout
    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión de usuario")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Sesión cerrada correctamente");
    }

    // GET: Reservas del usuario
    @GetMapping("/{id}/reservas")
    @Operation(summary = "Obtener reservas de un usuario")
    public ResponseEntity<List<String>> obtenerReservasDeUsuario(@PathVariable Long id) {
        List<String> reservas = new ArrayList<>();
        reservas.add("Reserva 1 del usuario " + id);
        reservas.add("Reserva 2 del usuario " + id);
        return ResponseEntity.ok(reservas);
    }

    // GET: Suscripciones del usuario
    @GetMapping("/{id}/suscripciones")
    @Operation(summary = "Obtener suscripciones de un usuario")
    public ResponseEntity<List<String>> obtenerSuscripcionesDeUsuario(@PathVariable Long id) {
        List<String> suscripciones = new ArrayList<>();
        suscripciones.add("Suscripción 1 del usuario " + id);
        suscripciones.add("Suscripción 2 del usuario " + id);
        return ResponseEntity.ok(suscripciones);
    }
}