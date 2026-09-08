package com.ericajavaproagent.consertaai.infrastructure.adapter.in.web;

import com.ericajavaproagent.consertaai.application.facade.ConsertaAiFacade;
import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import com.ericajavaproagent.consertaai.domain.model.StatusOcorrencia;
import com.ericajavaproagent.consertaai.infrastructure.adapter.in.web.dto.AvancarStatusRequestDTO;
import com.ericajavaproagent.consertaai.infrastructure.adapter.in.web.dto.OcorrenciaResponseDTO;
import com.ericajavaproagent.consertaai.infrastructure.adapter.in.web.dto.RegistrarOcorrenciaRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Controller RESTful para gerenciamento de ocorrencias urbanas.
 * Segue as melhores praticas RESTful: substantivos no plural, retorno de Location Header no 201 Created.
 */
@RestController
@RequestMapping("/api/v1/ocorrencias")
@Tag(name = "Ocorrencias", description = "Endpoints para registro, consulta e ciclo de vida de ocorrencias de zeladoria urbana")
public class OcorrenciaController {

    private final ConsertaAiFacade facade;

    public OcorrenciaController(ConsertaAiFacade facade) {
        this.facade = facade;
    }

    @Operation(summary = "Registrar nova ocorrencia", description = "Cria uma ocorrencia calculando a prioridade automaticamente via Strategy Pattern.")
    @ApiResponse(responseCode = "201", description = "Ocorrencia criada com sucesso")
    @PostMapping
    public ResponseEntity<OcorrenciaResponseDTO> registrar(@Valid @RequestBody RegistrarOcorrenciaRequestDTO dto) {
        Ocorrencia ocorrencia = facade.registrarOcorrencia(
                dto.categoria(),
                dto.descricao(),
                dto.enderecoOuReferencia(),
                dto.latitude(),
                dto.longitude()
        );

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{protocolo}")
                .buildAndExpand(ocorrencia.getProtocolo())
                .toUri();

        return ResponseEntity.created(location).body(OcorrenciaResponseDTO.fromDomain(ocorrencia));
    }

    @Operation(summary = "Consultar ocorrencia por protocolo", description = "Retorna os detalhes e o estado atual da ocorrencia.")
    @ApiResponse(responseCode = "200", description = "Ocorrencia encontrada")
    @ApiResponse(responseCode = "404", description = "Protocolo nao encontrado")
    @GetMapping("/{protocolo}")
    public ResponseEntity<OcorrenciaResponseDTO> buscarPorProtocolo(@PathVariable UUID protocolo) {
        Ocorrencia ocorrencia = facade.buscarPorProtocolo(protocolo);
        return ResponseEntity.ok(OcorrenciaResponseDTO.fromDomain(ocorrencia));
    }

    @Operation(summary = "Listar todas as ocorrencias", description = "Retorna a lista de todas as ocorrencias cadastradas.")
    @GetMapping
    public ResponseEntity<List<OcorrenciaResponseDTO>> listarTodas() {
        List<OcorrenciaResponseDTO> lista = facade.listarTodas().stream()
                .map(OcorrenciaResponseDTO::fromDomain)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Avancar estado da ocorrencia", description = "Executa a transicao na maquina de estados (State Pattern).")
    @ApiResponse(responseCode = "200", description = "Estado atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Transicao de estado invalida")
    @PatchMapping("/{protocolo}/status")
    public ResponseEntity<OcorrenciaResponseDTO> avancarStatus(
            @PathVariable UUID protocolo,
            @Valid @RequestBody AvancarStatusRequestDTO dto) {

        StatusOcorrencia novoStatus = switch (dto.novoStatusTipo().toUpperCase()) {
            case "EM_ANALISE" -> new StatusOcorrencia.EmAnalise(LocalDateTime.now(), dto.responsavel() != null ? dto.responsavel() : "Equipe Padrao");
            case "RESOLVIDA" -> new StatusOcorrencia.Resolvida(LocalDateTime.now(), dto.observacaoOuMotivo() != null ? dto.observacaoOuMotivo() : "Servico Concluido");
            case "REABERTA" -> new StatusOcorrencia.Reaberta(LocalDateTime.now(), dto.observacaoOuMotivo() != null ? dto.observacaoOuMotivo() : "Problema Persiste");
            default -> throw new IllegalArgumentException("Tipo de status invalido: " + dto.novoStatusTipo());
        };

        Ocorrencia atualizada = facade.avancarStatus(protocolo, novoStatus);
        return ResponseEntity.ok(OcorrenciaResponseDTO.fromDomain(atualizada));
    }
}
