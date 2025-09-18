package ExpooCode.ExpooCode.business.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de un reporte")
public class ReporteDTO {

    @Schema(description = "ID único de el reporte", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
}
