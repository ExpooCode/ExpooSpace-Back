package ExpooCode.ExpooCode.presentation.Controller;

import ExpooCode.ExpooCode.persistence.entity.Factura;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/expooSpace/facturas")
@Tag(name = "Factura", description = "Gestión de facturas de los usuarios")
public class FacturaController {

    private List<Factura> facturas = new ArrayList<>();

    @Operation(summary = "Listar facturas", description = "Obtiene todas las facturas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de facturas obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Factura>> listarFacturas() {
        return ResponseEntity.ok(facturas);
    }

    @Operation(summary = "Obtener factura por ID", description = "Busca una factura específica por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura encontrada"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Factura> obtenerFactura(@PathVariable Long id) {
        return facturas.stream()
                .filter(f -> f.getIdFactura().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Descargar factura", description = "Genera el enlace de descarga de una factura por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enlace de descarga generado"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/descargar")
    public ResponseEntity<String> descargarFactura(@PathVariable Long id) {
        return facturas.stream()
                .filter(f -> f.getIdFactura().equals(id))
                .findFirst()
                .map(f -> ResponseEntity.ok("Descargando factura desde: " + f.getUrlDescarga()))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener facturas por usuario", description = "Lista todas las facturas asociadas a un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de facturas obtenido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{id_usuario}")
    public ResponseEntity<List<Factura>> facturasPorUsuario(@PathVariable Long id_usuario) {
        List<Factura> resultado = facturas.stream()
                .filter(f -> f.getPago() != null
                        && f.getPago().getReserva() != null
                        && f.getPago().getReserva().getUsuario().getIdUsuario().equals(id_usuario))
                .toList();
        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Reenviar factura", description = "Permite reenviar una factura por correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura reenviada correctamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{id}/reenviar")
    public ResponseEntity<String> reenviarFactura(@PathVariable Long id) {
        return facturas.stream()
                .filter(f -> f.getIdFactura().equals(id))
                .findFirst()
                .map(f -> ResponseEntity.ok("Factura " + f.getNumeroFactura() + " reenviada por email"))
                .orElse(ResponseEntity.notFound().build());
    }
}
