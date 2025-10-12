package ExpooCode.ExpooCode.business.DTO;

import ExpooCode.ExpooCode.persistence.entity.Usuario;
import ExpooCode.ExpooCode.persistence.enums.EstadoSuscripcion;
import ExpooCode.ExpooCode.persistence.enums.TipoPlan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de una suscripción")
public class SuscripcionDTO {

    @Schema(description = "ID único de la suscripción", example = "200", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idSuscripcion;

    @Schema(description = "Id del usuario al cual pertenece la suscripcion", example = "123", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idUsuario;

    @Schema(description = "Tipo de plan contratado", example = "Basico", required = true)
    private TipoPlan tipoPlan;

    @Schema(description = "Cantidad de horas incluidas en el plan", example = "40", required = true)
    private Integer horasIncluidas;

    @Schema(description = "Precio mensual de la suscripción", example = "49999.99", required = true)
    private BigDecimal precioMensual;

    @Schema(description = "Fecha de inicio de la suscripción", example = "2025-01-01T08:00:00", required = true)
    private LocalDateTime fechaInicio;

    @Schema(description = "Fecha de fin de la suscripción", example = "2025-12-31T23:59:59", required = true)
    private LocalDateTime fechaFin;

    @Schema(description = "Estado actual de la suscripción", example = "Activa", required = true)
    private EstadoSuscripcion estado;

    @Schema(description = "Indica si la suscripción se renueva automáticamente", example = "true", required = true)
    private boolean renovacionAutomatica;
}
