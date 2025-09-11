package ExpooCode.ExpooCode.presentation.Controller;

import ExpooCode.ExpooCode.persistence.entity.Reserva;
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

@RestController
@RequestMapping("/api/expooSpace/reservas")
@Tag(name = "Reservas", description = "Gestión de reservas en el sistema (usuarios y Admin)")
public class ReservaController {

    private final Map<Long, Reserva> reservas = new HashMap<>();
    private Long currentId = 1L;

    @Operation(summary = "Listar reservas", description = "Obtiene todas las reservas registradas. Admin ve todas, usuarios solo las propias.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de reservas obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Reserva>> getAllReservas() {
        return ResponseEntity.ok(new ArrayList<>(reservas.values()));
    }

    @Operation(summary = "Obtener reserva por ID", description = "Consulta el detalle de una reserva específica (propietario o Admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservaById(@PathVariable Long id) {
        Reserva reserva = reservas.get(id);
        if (reserva == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró la reserva con id " + id);
        }
        return ResponseEntity.ok(reserva);
    }

    @Operation(summary = "Crear nueva reserva", description = "Permite a un usuario afiliado registrar una reserva en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Reserva> createReserva(@RequestBody Reserva reserva) {
        reserva.setIdReserva(currentId++);
        reservas.put(reserva.getIdReserva(), reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
    }

    @Operation(summary = "Actualizar reserva", description = "Permite modificar los datos de una reserva existente (propietario o Admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReserva(@PathVariable Long id, @RequestBody Reserva reserva) {
        if (!reservas.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se puede actualizar. La reserva con id " + id + " no existe.");
        }
        reserva.setIdReserva(id);
        reservas.put(id, reserva);
        return ResponseEntity.ok(reserva);
    }

    @Operation(summary = "Cancelar reserva", description = "Elimina una reserva registrada (propietario o Admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReserva(@PathVariable Long id) {
        if (!reservas.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se puede eliminar. La reserva con id " + id + " no existe.");
        }
        reservas.remove(id);
        return ResponseEntity.ok("Reserva con id " + id + " eliminada exitosamente.");
    }
}