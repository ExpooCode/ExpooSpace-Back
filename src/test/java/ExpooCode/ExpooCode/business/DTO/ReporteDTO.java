package ExpooCode.ExpooCode.business.DTO;

import ExpooCode.ExpooCode.persistence.enums.TipoReporte;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de un reporte")
public class ReporteDTO {

    @Schema(description = "ID único del reporte", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idReporte;

    @Schema(description = "ID del usuario que genera el reporte", example = "12", required = true)
    private Long idUsuario;

    @Schema(description = "Tipo de reporte", example = "Ocupacion", required = true)
    private TipoReporte tipo;

    @Schema(description = "Fecha de generación del reporte", example = "2025-04-05T09:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaGeneracion;

    @Schema(description = "Contenido del reporte", example = "Información detallada del reporte", accessMode = Schema.AccessMode.READ_ONLY)
    private String contenido;
}


