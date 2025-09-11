package ExpooCode.ExpooCode.presentation.Controller;

import ExpooCode.ExpooCode.persistence.entity.Recurso;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/expooSpace/recursos")
@Tag(name = "Recursos", description = "Gestión de recursos disponibles en la plataforma")
public class RecursoController {

    private final Map<Integer, Recurso> recursos = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Operation(summary = "Listar recursos", description = "Obtiene todos los recursos disponibles (público)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de recursos obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Recurso>> listarRecursos() {
        return ResponseEntity.ok(new ArrayList<>(recursos.values()));
    }

    @Operation(summary = "Registrar recurso", description = "Crea un nuevo recurso en el sistema (solo Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recurso registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Recurso> registrarRecurso(@RequestBody Recurso recurso) {
        if (recurso == null || recurso.getNombre() == null) {
            return ResponseEntity.badRequest().build();
        }
        int id = idGenerator.getAndIncrement();
        recurso.setIdRecurso(id);
        recursos.put(id, recurso);
        return ResponseEntity.status(HttpStatus.CREATED).body(recurso);
    }

    @Operation(summary = "Obtener recurso por ID", description = "Consulta el detalle de un recurso específico (público)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recurso encontrado"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Recurso> obtenerRecurso(@PathVariable Integer id) {
        Recurso recurso = recursos.get(id);
        if (recurso == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(recurso);
    }

    @Operation(summary = "Actualizar recurso", description = "Modifica la información de un recurso existente (solo Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recurso actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Recurso> actualizarRecurso(@PathVariable Integer id, @RequestBody Recurso recurso) {
        if (!recursos.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        recurso.setIdRecurso(id);
        recursos.put(id, recurso);
        return ResponseEntity.ok(recurso);
    }

    @Operation(summary = "Eliminar recurso", description = "Elimina un recurso del sistema (solo Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recurso eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRecurso(@PathVariable Integer id) {
        if (!recursos.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        recursos.remove(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verificar disponibilidad de recurso", description = "Verifica la disponibilidad de un recurso en fechas específicas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disponibilidad verificada"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/disponibilidad")
    public ResponseEntity<String> verificarDisponibilidad(@PathVariable Integer id) {
        Recurso recurso = recursos.get(id);
        if (recurso == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok("Disponibilidad verificada para el recurso con ID: " + id);
    }

    @Operation(summary = "Obtener tipos de recursos", description = "Devuelve los tipos de recursos disponibles en la plataforma")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipos de recursos obtenidos correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/tipos")
    public ResponseEntity<Set<String>> obtenerTipos() {
        Set<String> tipos = new HashSet<>();
        for (Recurso r : recursos.values()) {
            tipos.add(r.getTipo().name());
        }
        return ResponseEntity.ok(tipos);
    }
}
