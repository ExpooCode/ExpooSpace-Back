package ExpooCode.ExpooCode.presentation.Controller;

import ExpooCode.ExpooCode.business.DTO.ReporteDTO;
import ExpooCode.ExpooCode.business.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expooSpace/reportes")
@Tag(name = "Reportes", description = "Gestión y generación de reportes (solo Admin)")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @Operation(summary = "Listar reportes")
    @GetMapping
    public ResponseEntity<List<ReporteDTO>> listarReportes() {
        return ResponseEntity.ok(reporteService.listarReportes());
    }

    @Operation(summary = "Generar reporte")
    @PostMapping
    public ResponseEntity<ReporteDTO> generarReporte(@RequestBody ReporteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.generarReporte(dto));
    }

    @Operation(summary = "Obtener reporte por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReporteDTO> obtenerReporte(@PathVariable Long id) {
        ReporteDTO reporte = reporteService.obtenerReporte(id);
        return reporte != null ? ResponseEntity.ok(reporte) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar reporte por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        return reporteService.eliminarReporte(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Generar reporte de ocupación")
    @PostMapping("/ocupacion")
    public ResponseEntity<ReporteDTO> generarReporteOcupacion() {
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.generarReporteOcupacion());
    }

    @Operation(summary = "Generar reporte de ingresos")
    @PostMapping("/ingresos")
    public ResponseEntity<ReporteDTO> generarReporteIngresos() {
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.generarReporteIngresos());
    }
}
