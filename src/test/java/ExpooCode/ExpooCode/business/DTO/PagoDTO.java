package ExpooCode.ExpooCode.business.DTO;


import ExpooCode.ExpooCode.persistence.enums.EstadoPago;
import ExpooCode.ExpooCode.persistence.enums.MetodoPago;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de un pago realizado en el sistema")
public class PagoDTO {

    @Schema(description = "ID único del pago", example = "501", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idPago;

    @Schema(description = "ID de la reserva asociada al pago (si aplica)", example = "301", required = false)
    private Long idReserva;

    @Schema(description = "ID de la suscripción asociada al pago (si aplica)", example = "45", required = false)
    private Long idSuscripcion;

    @Schema(description = "Monto del pago", example = "150000.00", required = true)
    private BigDecimal monto;

    @Schema(description = "Método de pago utilizado", example = "Tarjeta", required = true)
    private MetodoPago metodo;

    @Schema(description = "Estado actual del pago", example = "Pendiente", required = true)
    private EstadoPago estado;

    @Schema(description = "Fecha en la que se realizó el pago", example = "2025-06-15T14:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaPago;

    @Schema(description = "ID de la factura generada (si existe)", example = "9001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idFactura;
}
