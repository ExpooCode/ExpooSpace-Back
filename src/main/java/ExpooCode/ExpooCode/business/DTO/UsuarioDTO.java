package ExpooCode.ExpooCode.business.DTO;

import ExpooCode.ExpooCode.persistence.enums.EstadoUsuario;
import ExpooCode.ExpooCode.persistence.enums.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del usuario")
public class UsuarioDTO {

    @Schema(description = "ID único del usuario", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idUsuario;

    @Schema(description = "Nombre completo del usuario", example = "Carlos perez", required = true, maxLength = 100)
    private String nombre;

    @Schema(description = "Correo electrónico del usuario", example = "Carlos.perez@email.com", required = true)
    private String email;

    @Schema(description = "Contraseña del usuario", example = "Password123", required = true, minLength = 8)
    private String password;

    @Schema(description = "Rol asignado al usuario", example = "Administrador", required = true)
    private RolUsuario rol;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado actual del usuario", example = "Activo", required = true)
    private EstadoUsuario estado;
}
