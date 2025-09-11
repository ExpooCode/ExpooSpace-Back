package ExpooCode.ExpooCode.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // Le dice a Spring que esta es una clase de configuración
@OpenAPIDefinition(  // Define la información general de tu API
        info = @Info(
                title = "Tienda Virtual API",
                version = "1.0.0",
                description = "API REST para la gestión completa de una tienda virtual",  // Descripción
                contact = @Contact(
                        name = "Construccion de Apps Empresariales",
                        email = "",
                        url = "eam.edu.co"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Servidor de Desarrollo"
                ),
                @Server(
                        url = "http://localhost:8090",
                        description = "Servidor de Pruebas"
                ),
                @Server(
                        url = "http://localhost:8099",
                        description = "Servidor de Producción"
                )
        }
)

public class OpenApiConfig {

    @Bean  // Crea un objeto que Spring va a usar para configurar OpenAPI
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        // ESTO ES PARA AUTENTICACIÓN JWT (si la usas más adelante)
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)  // Tipo de autenticación HTTP
                                        .scheme("bearer")                // Esquema Bearer
                                        .bearerFormat("JWT")             // Formato del token
                                        .description("Ingresa tu token JWT")  // Descripción para el usuario
                        )
                );
    }
}