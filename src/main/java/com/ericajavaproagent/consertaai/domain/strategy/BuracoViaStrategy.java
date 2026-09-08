package com.ericajavaproagent.consertaai.domain.strategy;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;
import org.springframework.stereotype.Component;

/**
 * Estrategia de priorizacao para a categoria BURACO_VIA.
 */
@Component
public class BuracoViaStrategy implements PriorizacaoStrategy {

    @Override
    public boolean suporta(Categoria categoria) {
        return categoria == Categoria.BURACO_VIA;
    }

    @Override
    public Prioridade calcularPrioridade(String descricao, double latitude, double longitude) {
        if (descricao != null) {
            String descLower = descricao.toLowerCase();
            if (descLower.contains("cratera") || descLower.contains("acidente") || descLower.contains("avenida")) {
                return Prioridade.CRITICA;
            }
        }
        return Prioridade.ALTA;
    }
}
