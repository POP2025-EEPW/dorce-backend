package project.dorce;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Dorce API"))
@SecurityScheme(
        name = "Token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "UUID/Custom",
        description = "Custom Authorization header using the 'Token' scheme. Header format: Token {UUID/Token}"
)
public class OpenApiConfig {

}