package ExpooCode.ExpooCode.presentation.Controller;

import ExpooCode.ExpooCode.business.DTO.BloqueoRecursoDTO;
import ExpooCode.ExpooCode.business.service.BloqueoRecursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expooSpace/bloqueos")
@Tag(name = "Bloqueo de Recursos", description = "Gestión de bloqueos de recursos por mantenimiento u otras razones")
public class BloqueoRecursoController {

    @Autowired
    private BloqueoRecursoService bloqueoRecursoService;

    @Operation(summary = "Listar bloqueos de recursos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<BloqueoRecursoDTO>> listarBloqueos() {
        return ResponseEntity.ok(bloqueoRecursoService.listarBloqueos());
    }

    @Operation(summary = "Obtener un bloqueo por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bloqueo encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe el bloqueo")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BloqueoRecursoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bloqueoRecursoService.obtenerPorId(id));
    }

    @Operation(summary = "Crear un nuevo bloqueo de recurso")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Bloqueo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<BloqueoRecursoDTO> crearBloqueo(@RequestBody BloqueoRecursoDTO dto) {
        BloqueoRecursoDTO nuevo = bloqueoRecursoService.crearBloqueo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Operation(summary = "Eliminar un bloqueo existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Bloqueo eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el bloqueo")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBloqueo(@PathVariable Long id) {
        bloqueoRecursoService.eliminarBloqueo(id);
        return ResponseEntity.noContent().build();
    }
}
