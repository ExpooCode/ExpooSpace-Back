package ExpooCode.ExpooCode.business.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información sobre un bloqueo aplicado a un recurso")
public class BloqueoRecursoDTO {

    @Schema(description = "ID único del bloqueo", example = "3001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idBloqueo;

    @Schema(description = "ID del recurso bloqueado", example = "15", required = true)
    private Integer idRecurso;

    @Schema(description = "Fecha de inicio del bloqueo", example = "2025-09-20T08:00:00", required = true)
    private LocalDateTime fechaInicio;

    @Schema(description = "Fecha de fin del bloqueo", example = "2025-09-20T18:00:00", required = true)
    private LocalDateTime fechaFin;

    @Schema(description = "Motivo del bloqueo", example = "MANTENIMIENTO", required = true)
    private String motivo;

    @Schema(description = "Descripción adicional del bloqueo", example = "Revisión técnica programada", required = false)
    private String descripcion;

    @Schema(description = "Indica si el bloqueo está activo", example = "true", required = true)
    private boolean activo;
}
