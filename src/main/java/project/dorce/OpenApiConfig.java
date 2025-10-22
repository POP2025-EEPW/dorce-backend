package project.dorce;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Dorce API"))
@SecurityScheme(
        name = "AuthToken",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "Bearer",
        description = "Custom Authorization header using the 'Bearer' scheme. Header format: Bearer {UUID/Token}"
)
public class OpenApiConfig {

}