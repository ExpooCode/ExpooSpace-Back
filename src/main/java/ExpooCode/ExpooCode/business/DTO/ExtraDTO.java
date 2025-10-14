package ExpooCode.ExpooCode.business.DTO;

import ExpooCode.ExpooCode.persistence.enums.TipoExtra;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de un extra disponible en el sistema")
public class ExtraDTO {

    @Schema(description = "ID único del extra", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idExtra;

    @Schema(description = "Nombre del extra", example = "Proyector Epson X200", required = true)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipo de extra", example = "Proyector", required = true)
    private TipoExtra tipoExtra;
}
