package ExpooCode.ExpooCode.presentation.Controller;

import ExpooCode.ExpooCode.business.DTO.RecursoDTO;
import ExpooCode.ExpooCode.business.service.RecursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/expooSpace/recursos")
@Tag(name = "Recursos", description = "Gestión de recursos disponibles en la plataforma")
public class RecursoController {

    private final RecursoService recursoService;

    public RecursoController(RecursoService recursoService) {
        this.recursoService = recursoService;
    }

    @Operation(summary = "Listar recursos", description = "Obtiene todos los recursos disponibles (público)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de recursos obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<RecursoDTO>> listarRecursos() {
        return ResponseEntity.ok(recursoService.getAllRecursos());
    }

    @Operation(summary = "Registrar recurso", description = "Crea un nuevo recurso en el sistema (solo Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recurso registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<RecursoDTO> registrarRecurso(@RequestBody RecursoDTO recursoDTO) {
        if (recursoDTO == null || recursoDTO.getNombre() == null) {
            return ResponseEntity.badRequest().build();
        }
        RecursoDTO creado = recursoService.createRecurso(recursoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Obtener recurso por ID", description = "Consulta el detalle de un recurso específico (público)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recurso encontrado"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RecursoDTO> obtenerRecurso(@PathVariable Long id) {
        return recursoService.getRecursoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Actualizar recurso", description = "Modifica la información de un recurso existente (solo Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recurso actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RecursoDTO> actualizarRecurso(@PathVariable Long id, @RequestBody RecursoDTO recursoDTO) {
        return recursoService.updateRecurso(id, recursoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Eliminar recurso", description = "Elimina un recurso del sistema (solo Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recurso eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRecurso(@PathVariable Long id) {
        boolean eliminado = recursoService.deleteRecurso(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verificar disponibilidad de recurso", description = "Verifica la disponibilidad de un recurso en fechas específicas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disponibilidad verificada"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/disponibilidad")
    public ResponseEntity<String> verificarDisponibilidad(@PathVariable Long id) {
        if (recursoService.getRecursoById(id).isEmpty()) {
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
        return ResponseEntity.ok(recursoService.getTiposDeRecursos());
    }
}
