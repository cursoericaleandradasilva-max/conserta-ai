package com.ericajavaproagent.consertaai;

import com.ericajavaproagent.consertaai.infrastructure.adapter.out.persistence.OcorrenciaJpaEntity;
import com.ericajavaproagent.consertaai.infrastructure.adapter.out.persistence.SpringDataOcorrenciaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = SpringDataOcorrenciaRepository.class)
@EntityScan(basePackageClasses = OcorrenciaJpaEntity.class)
public class ConsertaAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsertaAiApplication.class, args);
    }

}