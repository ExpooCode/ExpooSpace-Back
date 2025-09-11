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
@RequestMapping("/api/expooSpace/suscripciones")
@Tag(name = "Suscripciones", description = "Operaciones relacionadas con la gestión de suscripciones")
public class SuscripcionController {

    // GET: Listar todas las suscripciones
    @GetMapping
    @Operation(summary = "Listar suscripciones", description = "Obtiene todas las suscripciones (Admin ve todas, usuarios ven propias).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<String>> listarSuscripciones() {
        List<String> suscripciones = new ArrayList<>();
        suscripciones.add("Suscripción Básica");
        suscripciones.add("Suscripción Premium");
        return ResponseEntity.ok(suscripciones);
    }

    // POST: Crear suscripción
    @PostMapping
    @Operation(summary = "Crear una suscripción", description = "Permite a un usuario crear una nueva suscripción.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Suscripción creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> crearSuscripcion(
            @Parameter(description = "Tipo de plan", example = "Premium") @RequestParam String tipoPlan,
            @Parameter(description = "ID del usuario", example = "123") @RequestParam Long idUsuario
    ) {
        return ResponseEntity.status(201).body("Suscripción creada para usuario " + idUsuario + " con plan " + tipoPlan);
    }

    // GET: Obtener detalle de suscripción por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de suscripción", description = "Devuelve la información detallada de una suscripción.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción encontrada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada")
    })
    public ResponseEntity<String> obtenerSuscripcionPorId(
            @Parameter(description = "ID de la suscripción", example = "1") @PathVariable Long id
    ) {
        return ResponseEntity.ok("Detalle de suscripción con ID: " + id);
    }

    // PUT: Actualizar suscripción
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar suscripción", description = "Permite actualizar el plan de una suscripción existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> actualizarSuscripcion(
            @Parameter(description = "ID de la suscripción", example = "1") @PathVariable Long id,
            @Parameter(description = "Nuevo plan", example = "Corporativo") @RequestParam String nuevoPlan
    ) {
        return ResponseEntity.ok("Suscripción con ID " + id + " actualizada al plan " + nuevoPlan);
    }

    // DELETE: Cancelar suscripción
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar suscripción", description = "Permite cancelar una suscripción por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Suscripción cancelada exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> cancelarSuscripcion(
            @Parameter(description = "ID de la suscripción", example = "1") @PathVariable Long id
    ) {
        return ResponseEntity.noContent().build();
    }

    // GET: Tipos de planes disponibles
    @GetMapping("/planes")
    @Operation(summary = "Obtener tipos de planes disponibles", description = "Devuelve la lista de planes disponibles para suscripción.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de planes obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<String>> obtenerTiposDePlanes() {
        List<String> planes = new ArrayList<>();
        planes.add("Básico");
        planes.add("Premium");
        planes.add("Corporativo");
        return ResponseEntity.ok(planes);
    }

    // POST: Renovar suscripción
    @PostMapping("/{id}/renovar")
    @Operation(summary = "Renovar suscripción", description = "Permite renovar una suscripción activa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción renovada exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> renovarSuscripcion(
            @Parameter(description = "ID de la suscripción", example = "1") @PathVariable Long id
    ) {
        return ResponseEntity.ok("Suscripción con ID " + id + " renovada exitosamente.");
    }
}
