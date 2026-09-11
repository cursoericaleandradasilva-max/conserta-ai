package com.ericajavaproagent.consertaai.infrastructure.adapter.out.persistence;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;
import com.ericajavaproagent.consertaai.domain.model.StatusOcorrencia;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 🏛️ DATA MAPPER PATTERN:
 * Converte de Entidade de Domínio para Entidade JPA e vice-versa.
 */
@Component
public class OcorrenciaMapper {

    public OcorrenciaJpaEntity toJpa(Ocorrencia domain) {
        String tipo = "ABERTA";
        LocalDateTime data = LocalDateTime.now();
        String resp = null;
        String obs = null;

        switch (domain.getStatus()) {
            case StatusOcorrencia.Aberta a -> {
                tipo = "ABERTA";
                data = a.criadoEm();
            }
            case StatusOcorrencia.EmAnalise e -> {
                tipo = "EM_ANALISE";
                data = e.iniciadoEm();
                resp = e.responsavel();
            }
            case StatusOcorrencia.Resolvida r -> {
                tipo = "RESOLVIDA";
                data = r.resolvidoEm();
                obs = r.observacao();
            }
            case StatusOcorrencia.Reaberta re -> {
                tipo = "REABERTA";
                data = re.reabertoEm();
                obs = re.motivo();
            }
        }

        String categoriaStr = domain.getCategoria() != null ? domain.getCategoria().name() : null;
        String prioridadeStr = domain.getPrioridade() != null ? domain.getPrioridade().name() : null;

        return new OcorrenciaJpaEntity(
                domain.getProtocolo(),
                categoriaStr,
                domain.getDescricao(),
                domain.getEnderecoOuReferencia(),
                domain.getLatitude(),
                domain.getLongitude(),
                prioridadeStr,
                tipo,
                data,
                resp,
                obs
        );
    }

    public Ocorrencia toDomain(OcorrenciaJpaEntity jpa) {
        StatusOcorrencia status = switch (jpa.getStatusTipo()) {
            case "ABERTA" -> new StatusOcorrencia.Aberta(jpa.getDataStatus());
            case "EM_ANALISE" -> new StatusOcorrencia.EmAnalise(jpa.getDataStatus(), jpa.getMetaResponsavel());
            case "RESOLVIDA" -> new StatusOcorrencia.Resolvida(jpa.getDataStatus(), jpa.getMetaObservacaoOuMotivo());
            case "REABERTA" -> new StatusOcorrencia.Reaberta(jpa.getDataStatus(), jpa.getMetaObservacaoOuMotivo());
            default -> new StatusOcorrencia.Aberta(LocalDateTime.now());
        };

        Categoria categoria = jpa.getCategoria() != null ? Categoria.valueOf(jpa.getCategoria()) : null;
        Prioridade prioridade = jpa.getPrioridade() != null ? Prioridade.valueOf(jpa.getPrioridade()) : null;

        return new Ocorrencia(
                jpa.getProtocolo(),
                categoria,
                jpa.getDescricao(),
                jpa.getEnderecoOuReferencia(),
                jpa.getLatitude(),
                jpa.getLongitude(),
                prioridade,
                status
        );
    }
}