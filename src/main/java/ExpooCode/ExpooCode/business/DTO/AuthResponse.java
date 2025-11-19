package ExpooCode.ExpooCode.business.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private boolean success;
    private String message;

    // Métodos helper
    public static AuthResponse success(String message) {
        return new AuthResponse(true, message);
    }

    public static AuthResponse error(String message) {
        return new AuthResponse(false, message);
    }
}

