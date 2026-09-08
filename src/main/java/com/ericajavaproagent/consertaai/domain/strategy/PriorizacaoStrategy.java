package com.ericajavaproagent.consertaai.domain.strategy;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;

/**
 * Strategy Pattern (GoF).
 * Contrato para algoritmos de calculo de prioridade de ocorrencias.
 */
public interface PriorizacaoStrategy {

    boolean suporta(Categoria categoria);

    Prioridade calcularPrioridade(String descricao, double latitude, double longitude);
}
