package com.ericajavaproagent.consertaai.domain.strategy;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;

/**
 * 🏛️ DESIGN PATTERN: Strategy Pattern (GoF)
 *
 * Define o contrato para diferentes algoritmos e regras de negócio de priorização.
 */
public interface PriorizacaoStrategy {

    boolean suporta(Categoria categoria);

    Prioridade calcularPrioridade(String descricao, double latitude, double longitude);
}
