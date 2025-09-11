package ExpooCode.ExpooCode.presentation.Controller;

import ExpooCode.ExpooCode.persistence.entity.Reporte;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/expooSpace/reportes")
@Tag(name = "Reportes", description = "Gestión y generación de reportes (solo Admin)")
public class ReporteController {

    private final Map<Long, Reporte> reportes = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Operation(summary = "Listar reportes", description = "Obtiene todos los reportes generados (solo Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de reportes obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Reporte>> listarReportes() {
        return ResponseEntity.ok(new ArrayList<>(reportes.values()));
    }

    @Operation(summary = "Generar reporte", description = "Crea un nuevo reporte en el sistema (solo Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reporte generado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Reporte> generarReporte(@RequestBody Reporte reporte) {
        if (reporte == null || reporte.getContenido() == null) {
            return ResponseEntity.badRequest().build();
        }
        long id = idGenerator.getAndIncrement();
        reporte.setIdReporte(id);
        reportes.put(id, reporte);
        return ResponseEntity.status(HttpStatus.CREATED).body(reporte);
    }

    @Operation(summary = "Obtener reporte por ID", description = "Consulta el detalle de un reporte específico (solo Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte encontrado"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reporte> obtenerReporte(@PathVariable Long id) {
        Reporte reporte = reportes.get(id);
        if (reporte == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(reporte);
    }

    @Operation(summary = "Exportar reporte", description = "Exporta un reporte generado en formato CSV/Excel (solo Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte exportado correctamente"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/exportar")
    public ResponseEntity<String> exportarReporte(@PathVariable Long id) {
        Reporte reporte = reportes.get(id);
        if (reporte == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok("Exportando reporte con ID: " + id);
    }

    @Operation(summary = "Generar reporte de ocupación", description = "Genera un reporte específico de ocupación (solo Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reporte de ocupación generado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/ocupacion")
    public ResponseEntity<String> generarReporteOcupacion() {
        return ResponseEntity.status(HttpStatus.CREATED).body("Reporte de ocupación generado");
    }

    @Operation(summary = "Generar reporte de ingresos", description = "Genera un reporte específico de ingresos (solo Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reporte de ingresos generado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/ingresos")
    public ResponseEntity<String> generarReporteIngresos() {
        return ResponseEntity.status(HttpStatus.CREATED).body("Reporte de ingresos generado");
    }
}
