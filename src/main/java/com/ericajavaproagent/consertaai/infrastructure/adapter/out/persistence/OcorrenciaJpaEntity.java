package com.ericajavaproagent.consertaai.infrastructure.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ocorrencias")
public class OcorrenciaJpaEntity {

    @Id
    private UUID protocolo;
    private String categoria;
    private String descricao;
    private String enderecoOuReferencia;
    private Double latitude;
    private Double longitude;
    private String prioridade;
    private String statusTipo;
    private LocalDateTime dataStatus;
    private String metaResponsavel;
    private String metaObservacaoOuMotivo;

    public OcorrenciaJpaEntity() {
    }

    public OcorrenciaJpaEntity(UUID protocolo, String categoria, String descricao,
                               String enderecoOuReferencia, Double latitude, Double longitude,
                               String prioridade, String statusTipo, LocalDateTime dataStatus,
                               String metaResponsavel, String metaObservacaoOuMotivo) {
        this.protocolo = protocolo;
        this.categoria = categoria;
        this.descricao = descricao;
        this.enderecoOuReferencia = enderecoOuReferencia;
        this.latitude = latitude;
        this.longitude = longitude;
        this.prioridade = prioridade;
        this.statusTipo = statusTipo;
        this.dataStatus = dataStatus;
        this.metaResponsavel = metaResponsavel;
        this.metaObservacaoOuMotivo = metaObservacaoOuMotivo;
    }

    public UUID getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(UUID protocolo) {
        this.protocolo = protocolo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEnderecoOuReferencia() {
        return enderecoOuReferencia;
    }

    public void setEnderecoOuReferencia(String enderecoOuReferencia) {
        this.enderecoOuReferencia = enderecoOuReferencia;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public String getStatusTipo() {
        return statusTipo;
    }

    public void setStatusTipo(String statusTipo) {
        this.statusTipo = statusTipo;
    }

    public LocalDateTime getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(LocalDateTime dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getMetaResponsavel() {
        return metaResponsavel;
    }

    public void setMetaResponsavel(String metaResponsavel) {
        this.metaResponsavel = metaResponsavel;
    }

    public String getMetaObservacaoOuMotivo() {
        return metaObservacaoOuMotivo;
    }

    public void setMetaObservacaoOuMotivo(String metaObservacaoOuMotivo) {
        this.metaObservacaoOuMotivo = metaObservacaoOuMotivo;
    }
}