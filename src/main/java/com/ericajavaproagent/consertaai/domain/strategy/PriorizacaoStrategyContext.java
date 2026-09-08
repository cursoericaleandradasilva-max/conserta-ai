package com.ericajavaproagent.consertaai.domain.strategy;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contexto do Strategy Pattern gerenciado pelo Spring.
 */
@Service
public class PriorizacaoStrategyContext {

    private final List<PriorizacaoStrategy> strategies;

    public PriorizacaoStrategyContext(List<PriorizacaoStrategy> strategies) {
        this.strategies = strategies;
    }

    public Prioridade resolverPrioridade(Categoria categoria, String descricao, double latitude, double longitude) {
        return strategies.stream()
                .filter(s -> s.suporta(categoria))
                .findFirst()
                .map(s -> s.calcularPrioridade(descricao, latitude, longitude))
                .orElse(Prioridade.MEDIA);
    }
}
