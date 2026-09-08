package com.ericajavaproagent.consertaai.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade de Dominio Rica (Rich Domain Model).
 * Encapsula as regras de negocio e protege a transicao de estados (State Pattern).
 */
public final class Ocorrencia {

    private final UUID protocolo;
    private final Categoria categoria;
    private final String descricao;
    private final String enderecoOuReferencia;
    private final double latitude;
    private final double longitude;
    private Prioridade prioridade;
    private StatusOcorrencia status;

    public Ocorrencia(UUID protocolo, Categoria categoria, String descricao, String enderecoOuReferencia,
                      double latitude, double longitude, Prioridade prioridade, StatusOcorrencia status) {
        this.protocolo = Objects.requireNonNull(protocolo, "protocolo e obrigatorio");
        this.categoria = Objects.requireNonNull(categoria, "categoria e obrigatoria");
        this.descricao = Objects.requireNonNullElse(descricao, "Sem descricao informada");
        this.enderecoOuReferencia = Objects.requireNonNull(enderecoOuReferencia, "endereco ou referencia e obrigatorio");
        this.latitude = latitude;
        this.longitude = longitude;
        this.prioridade = Objects.requireNonNullElse(prioridade, Prioridade.MEDIA);
        this.status = Objects.requireNonNull(status, "status e obrigatorio");
    }

    /**
     * Factory Method para criacao de nova ocorrencia no estado inicial Aberta.
     */
    public static Ocorrencia registrar(Categoria categoria, String descricao, String enderecoOuReferencia,
                                       double latitude, double longitude, Prioridade prioridade) {
        return new Ocorrencia(
                UUID.randomUUID(),
                categoria,
                descricao,
                enderecoOuReferencia,
                latitude,
                longitude,
                prioridade,
                new StatusOcorrencia.Aberta(LocalDateTime.now())
        );
    }

    /**
     * Valida e executa a transicao de estado usando pattern matching.
     */
    public void avancarPara(StatusOcorrencia novoStatus) {
        boolean valido = switch (this.status) {
            case StatusOcorrencia.Aberta a -> novoStatus instanceof StatusOcorrencia.EmAnalise;
            case StatusOcorrencia.EmAnalise e -> novoStatus instanceof StatusOcorrencia.Resolvida;
            case StatusOcorrencia.Resolvida r -> novoStatus instanceof StatusOcorrencia.Reaberta;
            case StatusOcorrencia.Reaberta re -> novoStatus instanceof StatusOcorrencia.EmAnalise;
        };

        if (!valido) {
            throw new IllegalStateException(
                    "Transicao de estado invalida: %s -> %s".formatted(this.status, novoStatus));
        }
        this.status = novoStatus;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public UUID getProtocolo() {
        return protocolo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEnderecoOuReferencia() {
        return enderecoOuReferencia;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public StatusOcorrencia getStatus() {
        return status;
    }
}
