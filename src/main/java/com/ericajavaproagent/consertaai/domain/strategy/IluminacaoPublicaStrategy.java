package com.ericajavaproagent.consertaai.domain.strategy;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;
import org.springframework.stereotype.Component;

@Component
public class IluminacaoPublicaStrategy implements PriorizacaoStrategy {

    @Override
    public boolean suporta(Categoria categoria) {
        return categoria == Categoria.ILUMINACAO_PUBLICA;
    }

    @Override
    public Prioridade calcularPrioridade(String descricao, double latitude, double longitude) {
        if (descricao != null && (descricao.toLowerCase().contains("escola") || descricao.toLowerCase().contains("hospital") || descricao.toLowerCase().contains("posto"))) {
            return Prioridade.ALTA;
        }
        return Prioridade.MEDIA;
    }
}
