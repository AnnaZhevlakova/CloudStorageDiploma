package com.example.CloudStorageDiploma.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI imageConversionOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("")
                        .description("")
                        .version("1.0.0"));
    }

}
