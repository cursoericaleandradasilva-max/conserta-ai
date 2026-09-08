package com.ericajavaproagent.consertaai.domain.strategy;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;
import org.springframework.stereotype.Component;

@Component
public class LixoAcumuladoStrategy implements PriorizacaoStrategy {

    @Override
    public boolean suporta(Categoria categoria) {
        return categoria == Categoria.LIXO_ACUMULADO;
    }

    @Override
    public Prioridade calcularPrioridade(String descricao, double latitude, double longitude) {
        if (descricao != null && (descricao.toLowerCase().contains("dengue") || descricao.toLowerCase().contains("enchente") || descricao.toLowerCase().contains("bueiro"))) {
            return Prioridade.CRITICA;
        }
        return Prioridade.MEDIA;
    }
}
