package com.ericajavaproagent.consertaai.domain;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;
import com.ericajavaproagent.consertaai.domain.model.StatusOcorrencia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 🧪 Testes de Domínio Puro (Regras de Negócio e State Pattern)
 */
class OcorrenciaTest {

    @Test
    @DisplayName("Deve nascer sempre no estado inicial Aberta")
    void deveNascerSempreNoEstadoAberta() {
        Ocorrencia ocorrencia = Ocorrencia.registrar(
                Categoria.BURACO_VIA, "Cratera na via", "Rua das Flores, 123", -19.92, -43.94, Prioridade.ALTA);

        assertThat(ocorrencia.getStatus()).isInstanceOf(StatusOcorrencia.Aberta.class);
    }

    @Test
    @DisplayName("Deve permitir transição válida de Aberta para EmAnalise")
    void devePermitirTransicaoDeAbertaParaEmAnalise() {
        Ocorrencia ocorrencia = Ocorrencia.registrar(
                Categoria.ILUMINACAO_PUBLICA, "Poste apagado", "Av. Central, 500", -19.92, -43.94, Prioridade.MEDIA);

        ocorrencia.avancarPara(new StatusOcorrencia.EmAnalise(LocalDateTime.now(), "equipe-zeladoria"));

        assertThat(ocorrencia.getStatus()).isInstanceOf(StatusOcorrencia.EmAnalise.class);
    }

    @Test
    @DisplayName("Não deve permitir pular de Aberta direto para Resolvida (State Pattern)")
    void naoDevePermitirPularDeAbertaDiretoParaResolvida() {
        Ocorrencia ocorrencia = Ocorrencia.registrar(
                Categoria.LIXO_ACUMULADO, "Entulho na praça", "Praça da Estação", -19.92, -43.94, Prioridade.BAIXA);

        assertThatThrownBy(() ->
                ocorrencia.avancarPara(new StatusOcorrencia.Resolvida(LocalDateTime.now(), "concluído")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Transição de estado inválida");
    }

    @Test
    @DisplayName("Deve permitir ciclo completo: Aberta -> EmAnalise -> Resolvida -> Reaberta -> EmAnalise")
    void devePermitirReabrirUmaOcorrenciaResolvidaEVoltarParaAnalise() {
        Ocorrencia ocorrencia = Ocorrencia.registrar(
                Categoria.BURACO_VIA, "Buraco", "Rua B, 45", -19.92, -43.94, Prioridade.ALTA);

        ocorrencia.avancarPara(new StatusOcorrencia.EmAnalise(LocalDateTime.now(), "equipe-obras"));
        ocorrencia.avancarPara(new StatusOcorrencia.Resolvida(LocalDateTime.now(), "buraco tapado"));
        ocorrencia.avancarPara(new StatusOcorrencia.Reaberta(LocalDateTime.now(), "buraco voltou a abrir"));

        assertThat(ocorrencia.getStatus()).isInstanceOf(StatusOcorrencia.Reaberta.class);

        ocorrencia.avancarPara(new StatusOcorrencia.EmAnalise(LocalDateTime.now(), "equipe-obras"));
        assertThat(ocorrencia.getStatus()).isInstanceOf(StatusOcorrencia.EmAnalise.class);
    }
}
