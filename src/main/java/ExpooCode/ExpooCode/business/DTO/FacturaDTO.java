package ExpooCode.ExpooCode.business.DTO;

import ExpooCode.ExpooCode.persistence.enums.TipoFactura;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de una factura generada en el sistema")
public class FacturaDTO {

    @Schema(description = "ID único de la factura", example = "9001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idFactura;

    @Schema(description = "ID del pago asociado a la factura", example = "501", required = true)
    private Long idPago;

    @Schema(description = "Número único de la factura", example = "FAC001", accessMode = Schema.AccessMode.READ_ONLY)
    private String numeroFactura;

    @Schema(description = "Fecha de emisión de la factura", example = "2025-08-20T11:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaEmision;

    @Schema(description = "Tipo de factura", example = "Reserva", required = true)
    private TipoFactura tipo;

    @Schema(description = "Subtotal de la factura", example = "120000.00", required = true)
    private BigDecimal subtotal;

    @Schema(description = "IVA aplicado en la factura", example = "22800.00", required = true)
    private BigDecimal iva;

    @Schema(description = "Total de la factura", example = "142800.00", required = true)
    private BigDecimal total;

    @Schema(description = "URL de descarga de la factura", example = "https://ExpooSpace.com/Factura21.pdf", accessMode = Schema.AccessMode.READ_ONLY)
    private String urlDescarga;
}
