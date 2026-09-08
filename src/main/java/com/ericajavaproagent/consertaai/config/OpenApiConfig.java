package com.ericajavaproagent.consertaai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracao da documentacao OpenAPI 3 / Swagger.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ConsertaAI - API de Zeladoria Urbana Inteligente")
                        .version("1.0.0")
                        .description("Plataforma RESTful de zeladoria urbana desenvolvida com Java 21, Spring Boot 3, Design Patterns GoF e Inteligencia Artificial.")
                        .contact(new Contact()
                                .name("Erica Leandra Da Silva")
                                .url("https://github.com/cursoericaleandradasilva-max"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
