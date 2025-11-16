package ExpooCode.ExpooCode.business.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {



        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe ser válido")
        private String email;

        @NotBlank(message = "La contraseña es obligatoria")
        private String password;

}
