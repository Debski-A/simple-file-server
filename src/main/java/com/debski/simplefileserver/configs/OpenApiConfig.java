package com.debski.simplefileserver.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springShopOpenAPI(Set<String> supportedFileTypes) {
        return new OpenAPI()
                .info(new Info().title("SimpleFileServer API")
                        .description("Simple file server application")
                        .version("v0.0.1")
                        .description("Simple CRUD file server</br>" +
                                "<h2>Supported file types are:</br>" +
                                supportedFileTypes +
                                "</h2>"));
    }
}
