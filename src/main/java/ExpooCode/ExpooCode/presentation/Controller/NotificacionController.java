package ExpooCode.ExpooCode.presentation.Controller;

import ExpooCode.ExpooCode.persistence.entity.Notificacion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/expooSpace/notificaciones")
@Tag(name = "Notificación", description = "Gestión de notificaciones de los usuarios")
public class NotificacionController {

    private List<Notificacion> notificaciones = new ArrayList<>();

    @Operation(summary = "Listar notificaciones", description = "Obtiene todas las notificaciones registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de notificaciones obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Notificacion>> listarNotificaciones() {
        return ResponseEntity.ok(notificaciones);
    }

    @Operation(summary = "Enviar notificación", description = "Registra y envía una nueva notificación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notificación creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Notificacion> enviarNotificacion(@RequestBody Notificacion notificacion) {
        notificaciones.add(notificacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacion);
    }

    @Operation(summary = "Obtener notificación por ID", description = "Busca una notificación específica por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> obtenerNotificacion(@PathVariable Long id) {
        return notificaciones.stream()
                .filter(n -> n.getIdNotificacion().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Marcar notificación como leída", description = "Permite marcar una notificación como leída por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación marcada como leída"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}/marcar-leida")
    public ResponseEntity<Notificacion> marcarLeida(@PathVariable Long id) {
        for (Notificacion notificacion : notificaciones) {
            if (notificacion.getIdNotificacion().equals(id)) {
                notificacion.setLeido(true);
                return ResponseEntity.ok(notificacion);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar notificación", description = "Elimina una notificación de la lista por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notificación eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long id) {
        boolean eliminado = notificaciones.removeIf(n -> n.getIdNotificacion().equals(id));
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Contar notificaciones no leídas", description = "Obtiene la cantidad de notificaciones que aún no han sido leídas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/no-leidas")
    public ResponseEntity<Long> contarNoLeidas() {
        long cantidad = notificaciones.stream()
                .filter(n -> !n.isLeido())
                .count();
        return ResponseEntity.ok(cantidad);
    }

    @Operation(summary = "Marcar todas como leídas", description = "Permite marcar todas las notificaciones como leídas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Todas las notificaciones fueron marcadas como leídas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/marcar-todas-leidas")
    public ResponseEntity<Void> marcarTodasLeidas() {
        notificaciones.forEach(n -> n.setLeido(true));
        return ResponseEntity.noContent().build();
    }
}
