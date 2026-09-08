package com.ericajavaproagent.consertaai.infrastructure.adapter.in.web.dto;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrarOcorrenciaRequestDTO(
    @NotNull(message = "A categoria é obrigatória")
    @Schema(description = "Categoria do problema urbano", example = "BURACO_VIA")
    Categoria categoria,

    @NotBlank(message = "A descrição é obrigatória")
    @Schema(description = "Relato detalhado da ocorrência", example = "Cratera profunda na pista direita quebrando suspensões")
    String descricao,

    @NotBlank(message = "O endereço ou ponto de referência é obrigatório")
    @Schema(description = "Localização ou endereço aproximado", example = "Av. Paulista, 1000 - Bela Vista")
    String enderecoOuReferencia,

    @Schema(description = "Latitude GPS", example = "-23.5614")
    double latitude,

    @Schema(description = "Longitude GPS", example = "-46.6559")
    double longitude
) {}
