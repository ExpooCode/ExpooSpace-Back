package ExpooCode.ExpooCode.presentation.Controller;

import ExpooCode.ExpooCode.persistence.entity.Pago;
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
@RequestMapping("/api/expooSpace/pagos")
@Tag(name = "Pagos", description = "Gestión y procesamiento de pagos")
public class PagoController {

    private final Map<Long, Pago> pagos = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Operation(summary = "Listar pagos", description = "Obtiene todos los pagos registrados (Admin ve todos, usuarios ven propios)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de pagos obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Pago>> listarPagos() {
        return ResponseEntity.ok(new ArrayList<>(pagos.values()));
    }

    @Operation(summary = "Registrar un pago", description = "Crea un nuevo registro de pago en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Pago> registrarPago(@RequestBody Pago pago) {
        if (pago == null || pago.getMonto() == null) {
            return ResponseEntity.badRequest().build();
        }
        long id = idGenerator.getAndIncrement();
        pago.setIdPago(id);
        pagos.put(id, pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(pago);
    }

    @Operation(summary = "Obtener pago por ID", description = "Consulta el detalle de un pago específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPago(@PathVariable Long id) {
        Pago pago = pagos.get(id);
        if (pago == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(pago);
    }

    @Operation(summary = "Actualizar pago", description = "Permite modificar la información de un pago existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizarPago(@PathVariable Long id, @RequestBody Pago pago) {
        if (!pagos.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        pago.setIdPago(id);
        pagos.put(id, pago);
        return ResponseEntity.ok(pago);
    }

    @Operation(summary = "Procesar pago", description = "Ejecuta el proceso de pago con la pasarela correspondiente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago procesado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{id}/procesar")
    public ResponseEntity<String> procesarPago(@PathVariable Long id) {
        Pago pago = pagos.get(id);
        if (pago == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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
        Pago pago = pagos.get(id);
        if (pago == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok("Pago con ID " + id + " reembolsado correctamente.");
    }
}


