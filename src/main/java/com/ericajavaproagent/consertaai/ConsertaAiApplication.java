package com.ericajavaproagent.consertaai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Classe principal de inicializacao da aplicacao ConsertaAI.
 * Mapeamento explicito de componentes, repositorios e entidades para deploy em container.
 */
@SpringBootApplication(scanBasePackages = "com.ericajavaproagent.consertaai")
@EnableJpaRepositories(basePackages = "com.ericajavaproagent.consertaai")
@EntityScan(basePackages = "com.ericajavaproagent.consertaai")
public class ConsertaAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsertaAiApplication.class, args);
    }
}