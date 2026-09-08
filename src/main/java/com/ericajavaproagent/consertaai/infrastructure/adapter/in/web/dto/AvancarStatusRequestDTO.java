package com.ericajavaproagent.consertaai.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AvancarStatusRequestDTO(
    @NotBlank(message = "O tipo do novo status é obrigatório (EM_ANALISE, RESOLVIDA, REABERTA)")
    @Schema(description = "Tipo do novo estado: EM_ANALISE, RESOLVIDA ou REABERTA", example = "EM_ANALISE")
    String novoStatusTipo,

    @Schema(description = "Nome do responsável técnico (obrigatório se EM_ANALISE)", example = "Equipe de Pavimentação SP")
    String responsavel,

    @Schema(description = "Observação ou motivo (obrigatório se RESOLVIDA ou REABERTA)", example = "Equipe em deslocamento para vistoria")
    String observacaoOuMotivo
) {}
