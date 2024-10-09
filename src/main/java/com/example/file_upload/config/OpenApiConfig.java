package com.example.file_upload.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("File Upload API")
                        .version("1.0")
                        .description("API for uploading files and retrieving information about them")
                        .contact(new Contact()
                                .name("Andrei Pugatšov")
                                .email("email")
                                .url("URL")));
    }
}