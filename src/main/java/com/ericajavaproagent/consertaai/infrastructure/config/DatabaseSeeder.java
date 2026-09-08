package com.ericajavaproagent.consertaai.infrastructure.config;

import com.ericajavaproagent.consertaai.application.facade.ConsertaAiFacade;
import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import com.ericajavaproagent.consertaai.domain.model.StatusOcorrencia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Inicializador de dados de demonstracao no banco de dados.
 */
@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final ConsertaAiFacade facade;

    public DatabaseSeeder(ConsertaAiFacade facade) {
        this.facade = facade;
    }

    @Override
    public void run(String... args) {
        if (facade.listarTodas().isEmpty()) {
            log.info("Inicializando banco de dados com dados de exemplo...");

            // 1. Ocorrencia Aberta
            Ocorrencia oc1 = facade.registrarOcorrencia(
                    Categoria.BURACO_VIA,
                    "Cratera profunda na pista direita proximo ao cruzamento com a Av. Brigadeiro Luis Antonio.",
                    "Av. Paulista, 1500 - Bela Vista, Sao Paulo - SP",
                    -23.5614,
                    -46.6559
            );

            // 2. Ocorrencia Em Analise
            Ocorrencia oc2 = facade.registrarOcorrencia(
                    Categoria.ILUMINACAO_PUBLICA,
                    "Poste apagado em frente a entrada de emergencia do Hospital das Clinicas.",
                    "Rua Doutor Eneas de Carvalho Aguiar, 255 - Cerqueira Cesar, Sao Paulo - SP",
                    -23.5558,
                    -46.6711
            );
            facade.avancarStatus(oc2.getProtocolo(), new StatusOcorrencia.EmAnalise(LocalDateTime.now(), "Equipe Iluminacao SP - Setor Centro"));

            // 3. Ocorrencia Resolvida
            Ocorrencia oc3 = facade.registrarOcorrencia(
                    Categoria.LIXO_ACUMULADO,
                    "Descarte irregular de entulho e moveis bloqueando a passagem de pedestres na calcada.",
                    "Rua Augusta, 800 - Consolacao, Sao Paulo - SP",
                    -23.5521,
                    -46.6534
            );
            facade.avancarStatus(oc3.getProtocolo(), new StatusOcorrencia.EmAnalise(LocalDateTime.now(), "Equipe Limpeza Urbana"));
            facade.avancarStatus(oc3.getProtocolo(), new StatusOcorrencia.Resolvida(LocalDateTime.now(), "Remocao completa realizada com caminhao coletor"));

            log.info("Banco de dados populado com sucesso com 3 ocorrencias iniciais.");
        }
    }
}
