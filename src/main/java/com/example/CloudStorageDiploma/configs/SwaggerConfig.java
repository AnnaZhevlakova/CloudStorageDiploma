package com.example.CloudStorageDiploma.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI imageConversionOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        final String bearerFormat = "JWT";

        return new OpenAPI()
                .info(new Info()
                        .title("Моё API с JWT")
                        .version("1.0")
                        .description("API с авторизацией через JWT"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat(bearerFormat)
                                        .description("Вставьте JWT токен в формате: Bearer <token>")
                        )
                );
    }

}
