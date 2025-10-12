package ExpooCode.ExpooCode.presentation.Controller;

import ExpooCode.ExpooCode.business.DTO.PagoDTO;
import ExpooCode.ExpooCode.business.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expooSpace/pagos")
@Tag(name = "Pagos", description = "Gestión y procesamiento de pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @Operation(summary = "Listar pagos", description = "Obtiene todos los pagos registrados (Admin ve todos, usuarios ven propios)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de pagos obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<PagoDTO>> listarPagos() {
        return ResponseEntity.ok(pagoService.getAllPagos());
    }

    @Operation(summary = "Registrar un pago", description = "Crea un nuevo registro de pago en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<PagoDTO> registrarPago(@RequestBody PagoDTO pagoDTO) {
        if (pagoDTO == null || pagoDTO.getMonto() == null) {
            return ResponseEntity.badRequest().build();
        }
        PagoDTO nuevo = pagoService.createPago(pagoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Operation(summary = "Obtener pago por ID", description = "Consulta el detalle de un pago específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> obtenerPago(@PathVariable Long id) {
        return pagoService.getPagoById(id)
                .map(ResponseEntity::ok) // Si lo encuentra -> 200 con el pago
                .orElse(ResponseEntity.notFound().build()); // Si no existe -> 404
    }

    @Operation(summary = "Actualizar pago", description = "Permite modificar la información de un pago existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PagoDTO> actualizarPago(@PathVariable Long id, @RequestBody PagoDTO pagoDTO) {
        return pagoService.updatePago(id, pagoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Procesar pago", description = "Ejecuta el proceso de pago con la pasarela correspondiente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago procesado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{id}/procesar")
    public ResponseEntity<String> procesarPago(@PathVariable Long id) {
        pagoService.procesarPago(id);
        return ResponseEntity.ok("Pago con ID " + id + " procesado correctamente.");
    }

    @Operation(summary = "Reembolsar pago", description = "Procesa el reembolso de un pago ya realizado (solo Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago reembolsado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{id}/reembolsar")
    public ResponseEntity<String> reembolsarPago(@PathVariable Long id) {
        pagoService.reembolsarPago(id);
        return ResponseEntity.ok("Pago con ID " + id + " reembolsado correctamente.");
    }

    @Operation(summary = "Eliminar pago", description = "Elimina un pago registrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        pagoService.deletePago(id);
        return ResponseEntity.noContent().build();
    }
}
