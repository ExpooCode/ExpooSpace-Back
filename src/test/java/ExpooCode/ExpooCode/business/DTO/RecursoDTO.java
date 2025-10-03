package ExpooCode.ExpooCode.business.DTO;

import ExpooCode.ExpooCode.persistence.enums.EstadoRecurso;
import ExpooCode.ExpooCode.persistence.enums.Tipo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de un recurso disponible en el sistema")
public class RecursoDTO {

    @Schema(description = "ID único del recurso", example = "15", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idRecurso;

    @Schema(description = "Nombre del recurso", example = "Sala de Reuniones A", required = true)
    private String nombre;

    @Schema(description = "Tipo de recurso", example = "Escritorio", required = true)
    private Tipo tipo;

    @Schema(description = "Capacidad del recurso (número de personas o unidades)", example = "20", required = true)
    private Integer capacidad;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado del recurso", example = "Activo", required = true)
    private EstadoRecurso estadoRecurso;
}
