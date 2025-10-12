package ExpooCode.ExpooCode.business.DTO;

import ExpooCode.ExpooCode.persistence.enums.EstadoPago;
import ExpooCode.ExpooCode.persistence.enums.EstadoReserva;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de una reserva")
public class ReservaDTO {

    @Schema(description = "ID único de la reserva", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idReserva;

    @Schema(description = "ID del usuario que realiza la reserva", example = "10", required = true)
    private Long usuarioId;

    @Schema(description = "Nombre del usuario que realiza la reserva", example = "Rafael Pérez", accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioNombre;

    @Schema(description = "ID del recurso reservado", example = "5", required = true)
    private Long recursoId;

    @Schema(description = "Nombre del recurso reservado", example = "Sala de reuniones principal", accessMode = Schema.AccessMode.READ_ONLY)
    private String recursoNombre;

    @Schema(description = "Fecha y hora de inicio de la reserva", example = "2025-09-20T10:00:00", required = true)
    private LocalDateTime fechaInicio;

    @Schema(description = "Fecha y hora de fin de la reserva", example = "2025-09-20T12:00:00", required = true)
    private LocalDateTime fechaFin;

    @Schema(description = "Estado actual de la reserva", example = "Confirmada", required = true)
    private EstadoReserva estado;

    @Schema(description = "ID del pago asociado (si existe)", example = "5001")
    private Long pagoId;

    @Schema(description = "Estado del pago asociado", example = "Pagado", accessMode = Schema.AccessMode.READ_ONLY)
    private EstadoPago pagoEstado;
}
