package ExpooCode.ExpooCode.business.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de una notificación enviada a un usuario")
public class NotificacionDTO {

    @Schema(description = "ID único de la notificación", example = "2001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idNotificacion;

    @Schema(description = "ID del usuario que recibe la notificación", example = "12", required = true)
    private Long idUsuario;

    @Schema(description = "Mensaje de la notificación", example = "Tu reserva ha sido confirmada", required = true)
    private String mensaje;

    @Schema(description = "Fecha en la que se envió la notificación", example = "2025-07-18T10:15:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaEnvio;

    @Schema(description = "Indica si la notificación ya fue leída", example = "false", required = true)
    private boolean leido;
}
