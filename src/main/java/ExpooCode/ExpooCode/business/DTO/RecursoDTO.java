package ExpooCode.ExpooCode.business.DTO;

import ExpooCode.ExpooCode.persistence.entity.enums.EstadoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Tipo de recurso", example = "ESCRITORIO", required = true)
    private String tipo;

    @Schema(description = "Capacidad del recurso (número de personas o unidades)", example = "20", required = true)
    private Integer capacidad;

    @Schema(description = "Estado del recurso", example = "DISPONIBLE", required = true)
    private EstadoUsuario estadoUsuario;
}
