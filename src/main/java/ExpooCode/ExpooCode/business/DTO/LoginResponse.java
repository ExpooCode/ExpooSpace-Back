package ExpooCode.ExpooCode.business.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType; // "Bearer"
    private String email;
    private String role; // "ROLE_ADMINISTRADOR", etc.
}

