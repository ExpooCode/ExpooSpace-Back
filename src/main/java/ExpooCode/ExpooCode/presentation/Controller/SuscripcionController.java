package ExpooCode.ExpooCode.presentation.Controller;

import ExpooCode.ExpooCode.business.DTO.SuscripcionDTO;
import ExpooCode.ExpooCode.business.service.SuscripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expooSpace/suscripciones")
@Tag(name = "Suscripciones", description = "Gestión de suscripciones de los usuarios")
public class SuscripcionController {

    private final SuscripcionService suscripcionService;

    public SuscripcionController(SuscripcionService suscripcionService) {
        this.suscripcionService = suscripcionService;
    }

    @Operation(summary = "Listar suscripciones")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<SuscripcionDTO>> listar() {
        return ResponseEntity.ok(suscripcionService.listarSuscripciones());
    }

    @Operation(summary = "Crear suscripción")
    @ApiResponse(responseCode = "201", description = "Suscripción creada correctamente")
    @PostMapping
    public ResponseEntity<SuscripcionDTO> crear(@RequestBody SuscripcionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(suscripcionService.crearSuscripcion(dto));
    }

    @Operation(summary = "Obtener suscripción por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Suscripción encontrada"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SuscripcionDTO> obtener(@PathVariable Long id) {
        SuscripcionDTO dto = suscripcionService.obtenerSuscripcion(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Actualizar suscripción")
    @ApiResponse(responseCode = "200", description = "Suscripción actualizada correctamente")
    @PutMapping("/{id}")
    public ResponseEntity<SuscripcionDTO> actualizar(@PathVariable Long id, @RequestBody SuscripcionDTO dto) {
        SuscripcionDTO actualizada = suscripcionService.actualizarSuscripcion(id, dto);
        return actualizada != null ? ResponseEntity.ok(actualizada) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar suscripción")
    @ApiResponse(responseCode = "204", description = "Suscripción eliminada correctamente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminada = suscripcionService.eliminarSuscripcion(id);
        return eliminada ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
