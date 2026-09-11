package com.ericajavaproagent.consertaai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.ericajavaproagent.consertaai")
@EnableJpaRepositories(basePackages = "com.ericajavaproagent.consertaai")
@EntityScan(basePackages = "com.ericajavaproagent.consertaai")
public class ConsertaAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsertaAiApplication.class, args);
    }

}