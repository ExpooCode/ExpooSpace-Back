package ExpooCode.ExpooCode.presentation.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/expooSpace/bloqueos")
@Tag(name = "Bloqueos", description = "Operaciones relacionadas con la gestión de bloqueos de recursos")
public class BloqueoController {

    // Lista en memoria para pruebas
    private final Map<Long, String> bloqueos = new HashMap<>();
    private long nextId = 1;

    // ==================== ENDPOINTS ====================

    // GET /api/expooSpace/bloqueos
    @GetMapping
    @Operation(summary = "Listar todos los bloqueos", description = "Obtiene la lista de bloqueos registrados (solo Admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de bloqueos obtenida exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - solo Admin")
    })
    public ResponseEntity<List<String>> listarBloqueos() {
        return ResponseEntity.ok(new ArrayList<>(bloqueos.values())); // 200 OK
    }

    // POST /api/expooSpace/bloqueos
    @PostMapping
    @Operation(summary = "Registrar un nuevo bloqueo", description = "Crea un bloqueo para un recurso (solo Admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bloqueo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<String> crearBloqueo(
            @Parameter(description = "Descripción del bloqueo", required = true, example = "Mantenimiento programado")
            @RequestParam String descripcion
    ) {
        long id = nextId++;
        bloqueos.put(id, descripcion);
        return ResponseEntity.status(201).body("Bloqueo creado con ID: " + id + " - " + descripcion);
    }

    // GET /api/expooSpace/bloqueos/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Detalle de un bloqueo", description = "Obtiene la información de un bloqueo específico (solo Admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bloqueo encontrado"),
            @ApiResponse(responseCode = "404", description = "Bloqueo no encontrado")
    })
    public ResponseEntity<String> obtenerBloqueoPorId(
            @Parameter(description = "ID del bloqueo", required = true, example = "1")
            @PathVariable Long id
    ) {
        if (!bloqueos.containsKey(id)) {
            return ResponseEntity.status(404).body("Bloqueo no encontrado"); // 404 Not Found
        }
        return ResponseEntity.ok("Bloqueo con ID " + id + ": " + bloqueos.get(id)); // 200 OK
    }

    // PUT /api/expooSpace/bloqueos/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar bloqueo", description = "Modifica la información de un bloqueo (solo Admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bloqueo actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bloqueo no encontrado")
    })
    public ResponseEntity<String> actualizarBloqueo(
            @Parameter(description = "ID del bloqueo", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nueva descripción del bloqueo", required = true, example = "Actualización de mantenimiento")
            @RequestParam String nuevaDescripcion
    ) {
        if (!bloqueos.containsKey(id)) {
            return ResponseEntity.status(404).body("Bloqueo no encontrado");
        }
        bloqueos.put(id, nuevaDescripcion);
        return ResponseEntity.ok("Bloqueo actualizado con ID " + id + ": " + nuevaDescripcion); // 200 OK
    }

    // DELETE /api/expooSpace/bloqueos/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar bloqueo", description = "Elimina un bloqueo del sistema (solo Admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bloqueo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bloqueo no encontrado")
    })
    public ResponseEntity<Void> eliminarBloqueo(
            @Parameter(description = "ID del bloqueo a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        if (!bloqueos.containsKey(id)) {
            return ResponseEntity.notFound().build(); // 404
        }
        bloqueos.remove(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // ==================== ENDPOINTS ESPECÍFICOS ====================

    // GET /api/expooSpace/bloqueos/recurso/{id_recurso}
    @GetMapping("/recurso/{idRecurso}")
    @Operation(summary = "Bloqueos de un recurso", description = "Obtiene todos los bloqueos asociados a un recurso específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bloqueos encontrados"),
            @ApiResponse(responseCode = "404", description = "No se encontraron bloqueos para el recurso")
    })
    public ResponseEntity<String> obtenerBloqueosPorRecurso(
            @Parameter(description = "ID del recurso", required = true, example = "10")
            @PathVariable Long idRecurso
    ) {
        // Lógica dummy (si el recurso es par, hay bloqueos)
        if (idRecurso % 2 == 0) {
            return ResponseEntity.ok("Bloqueos del recurso con ID " + idRecurso);
        }
        return ResponseEntity.status(404).body("No se encontraron bloqueos para el recurso " + idRecurso);
    }

    // PUT /api/expooSpace/bloqueos/{id}/desactivar
    @PutMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar bloqueo", description = "Marca un bloqueo como inactivo sin eliminarlo (solo Admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bloqueo desactivado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bloqueo no encontrado")
    })
    public ResponseEntity<String> desactivarBloqueo(
            @Parameter(description = "ID del bloqueo", required = true, example = "1")
            @PathVariable Long id
    ) {
        if (!bloqueos.containsKey(id)) {
            return ResponseEntity.status(404).body("Bloqueo no encontrado");
        }
        return ResponseEntity.ok("Bloqueo con ID " + id + " desactivado"); // 200 OK
    }
}
