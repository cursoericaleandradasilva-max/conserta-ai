package com.ericajavaproagent.consertaai.domain.strategy;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;
import org.springframework.stereotype.Component;

@Component
public class DefaultStrategy implements PriorizacaoStrategy {

    @Override
    public boolean suporta(Categoria categoria) {
        return categoria == Categoria.OUTRO;
    }

    @Override
    public Prioridade calcularPrioridade(String descricao, double latitude, double longitude) {
        return Prioridade.BAIXA;
    }
}
