package com.ericajavaproagent.consertaai.infrastructure.adapter.in.web.dto;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;
import com.ericajavaproagent.consertaai.domain.model.StatusOcorrencia;

import java.util.UUID;

public record OcorrenciaResponseDTO(
    UUID protocolo,
    Categoria categoria,
    String descricao,
    String enderecoOuReferencia,
    double latitude,
    double longitude,
    Prioridade prioridade,
    String status,
    String detalheStatus
) {
    public static OcorrenciaResponseDTO fromDomain(Ocorrencia domain) {
        String statusNome = domain.getStatus().getClass().getSimpleName();
        String detalhe = switch (domain.getStatus()) {
            case StatusOcorrencia.Aberta a -> "Aberta em: " + a.criadoEm();
            case StatusOcorrencia.EmAnalise e -> "Em análise por: " + e.responsavel() + " desde " + e.iniciadoEm();
            case StatusOcorrencia.Resolvida r -> "Resolvida em: " + r.resolvidoEm() + " | Obs: " + r.observacao();
            case StatusOcorrencia.Reaberta re -> "Reaberta em: " + re.reabertoEm() + " | Motivo: " + re.motivo();
        };

        return new OcorrenciaResponseDTO(
                domain.getProtocolo(),
                domain.getCategoria(),
                domain.getDescricao(),
                domain.getEnderecoOuReferencia(),
                domain.getLatitude(),
                domain.getLongitude(),
                domain.getPrioridade(),
                statusNome,
                detalhe
        );
    }
}
