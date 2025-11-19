package ExpooCode.ExpooCode.presentation.Controller;


import ExpooCode.ExpooCode.business.DTO.*;
import ExpooCode.ExpooCode.business.service.UsuarioService;
import ExpooCode.ExpooCode.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsuarioService usuarioService;

    /**
     * POST /api/auth/login
     * Login con email y password
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {

        try {
            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generar tokens
            String accessToken = jwtTokenProvider.generateToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

            // Respuesta exitosa
            return ResponseEntity.ok(new LoginResponse(
                    accessToken,
                    refreshToken,
                    "Bearer",
                    authentication.getName(),
                    authentication.getAuthorities().iterator().next().getAuthority()
            ));

        } catch (BadCredentialsException e) {
            log.warn("Credenciales incorrectas: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.error("Email o contraseña incorrectos"));

        } catch (LockedException e) {
            log.warn("Cuenta bloqueada: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.LOCKED)
                    .body(AuthResponse.error("Tu cuenta está bloqueada"));

        } catch (DisabledException e) {
            log.warn("Cuenta inactiva: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(AuthResponse.error("Tu cuenta está inactiva"));

        } catch (Exception e) {
            log.error("Error en login: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthResponse.error("Error interno del servidor"));
        }
    }
    /**
     * POST /api/auth/register
     * Registro de nuevo usuario
     */
    @PostMapping("/register")
    @Operation(summary = "Registro público de usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "409", description = "El email ya está registrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            UsuarioDTO nuevoUsuario = usuarioService.registerPublicUser(request);

            log.info("Usuario registrado exitosamente: {}", nuevoUsuario.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AuthResponse.success("Usuario registrado exitosamente"));

        } catch (RuntimeException e) {
            // Si el mensaje contiene "email ya está registrado" -> 409
            if (e.getMessage().contains("email ya está registrado")) {
                log.warn("Email duplicado: {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(AuthResponse.error(e.getMessage()));
            }

            // Otros errores -> 500
            log.error("Error al registrar usuario: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthResponse.error("Error al registrar usuario"));
        }
    }
}