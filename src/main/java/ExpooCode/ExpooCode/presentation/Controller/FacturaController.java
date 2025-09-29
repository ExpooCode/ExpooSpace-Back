package ExpooCode.ExpooCode.presentation.Controller;

import ExpooCode.ExpooCode.business.DTO.FacturaDTO;
import ExpooCode.ExpooCode.business.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expooSpace/facturas")
@Tag(name = "Facturas", description = "Gestión de facturas de los usuarios")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @Operation(summary = "Listar facturas", description = "Obtiene todas las facturas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de facturas obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<FacturaDTO>> listarFacturas() {
        return ResponseEntity.ok(facturaService.getAllFacturas());
    }

    @Operation(summary = "Obtener factura por ID", description = "Busca una factura específica por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura encontrada"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> obtenerFactura(@PathVariable Long id) {
        return facturaService.getFacturaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Descargar factura", description = "Genera el enlace de descarga de una factura por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enlace de descarga generado"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/descargar")
    public ResponseEntity<String> descargarFactura(@PathVariable Long id) {
        return facturaService.getFacturaById(id)
                .map(f -> ResponseEntity.ok("Descargando factura desde: " + f.getUrlDescarga()))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Obtener facturas por usuario", description = "Lista todas las facturas asociadas a un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de facturas obtenido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<FacturaDTO>> facturasPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(facturaService.getFacturasByUsuario(idUsuario));
    }

    @Operation(summary = "Reenviar factura", description = "Permite reenviar una factura por correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura reenviada correctamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{id}/reenviar")
    public ResponseEntity<String> reenviarFactura(@PathVariable Long id) {
        return facturaService.getFacturaById(id)
                .map(f -> ResponseEntity.ok("Factura " + f.getNumeroFactura() + " reenviada por email"))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @Operation(summary = "Crear factura", description = "Permite crear una nueva factura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Factura creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<FacturaDTO> crearFactura(@RequestBody FacturaDTO facturaDTO) {
        FacturaDTO nuevaFactura = facturaService.createFactura(facturaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFactura);
    }


}
