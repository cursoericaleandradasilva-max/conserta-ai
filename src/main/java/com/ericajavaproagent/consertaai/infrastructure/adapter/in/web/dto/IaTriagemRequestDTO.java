package com.ericajavaproagent.consertaai.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record IaTriagemRequestDTO(
    @NotBlank(message = "O relato não pode estar vazio")
    @Schema(description = "Relato em texto ou transcrição de áudio do cidadão", example = "O poste em frente à escola municipal está sem luz há 3 dias e a rua está perigosa")
    String relato
) {}
