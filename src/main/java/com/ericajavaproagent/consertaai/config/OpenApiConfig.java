package com.ericajavaproagent.consertaai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 📖 Configuração do Swagger / OpenAPI 3 do ConsertaAI.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ConsertaAI - API de Zeladoria Urbana Inteligente")
                        .version("1.0.0")
                        .description("Plataforma de zeladoria urbana desenvolvida com Java 21, Spring Boot 3, GoF Design Patterns (State, Strategy, Facade, Template Method, Adapter) e Inteligência Artificial.")
                        .contact(new Contact()
                                .name("Erica Leandra Da Silva - Engenheira de Software Java")
                                .url("https://github.com/cursoericaleandradasilva-max"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
