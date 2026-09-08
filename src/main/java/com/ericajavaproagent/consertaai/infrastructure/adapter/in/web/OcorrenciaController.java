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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 🌐 CONTROLLER REST (Inbound Adapter):
 * Comunica com a aplicação através da Fachada (Facade Pattern).
 */
@RestController
@RequestMapping("/api/v1/ocorrencias")
@Tag(name = "🏙️ Ocorrências de Zeladoria Urbana", description = "Endpoints para registro, consulta e transição de estados de ocorrências urbanas")
public class OcorrenciaController {

    private final ConsertaAiFacade facade;

    public OcorrenciaController(ConsertaAiFacade facade) {
        this.facade = facade;
    }

    @Operation(summary = "Registrar nova ocorrência", description = "Cria uma ocorrência calculando a prioridade automaticamente via Strategy Pattern.")
    @ApiResponse(responseCode = "201", description = "Ocorrência registrada com sucesso")
    @PostMapping
    public ResponseEntity<OcorrenciaResponseDTO> registrar(@Valid @RequestBody RegistrarOcorrenciaRequestDTO dto) {
        Ocorrencia ocorrencia = facade.registrarOcorrencia(
                dto.categoria(),
                dto.descricao(),
                dto.enderecoOuReferencia(),
                dto.latitude(),
                dto.longitude()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(OcorrenciaResponseDTO.fromDomain(ocorrencia));
    }

    @Operation(summary = "Consultar ocorrência por protocolo", description = "Retorna os detalhes e o estado atual da ocorrência.")
    @ApiResponse(responseCode = "200", description = "Ocorrência encontrada")
    @ApiResponse(responseCode = "404", description = "Protocolo não encontrado")
    @GetMapping("/{protocolo}")
    public ResponseEntity<OcorrenciaResponseDTO> buscarPorProtocolo(@PathVariable UUID protocolo) {
        Ocorrencia ocorrencia = facade.buscarPorProtocolo(protocolo);
        return ResponseEntity.ok(OcorrenciaResponseDTO.fromDomain(ocorrencia));
    }

    @Operation(summary = "Listar todas as ocorrências", description = "Retorna a lista completa de ocorrências registradas na cidade.")
    @GetMapping
    public ResponseEntity<List<OcorrenciaResponseDTO>> listarTodas() {
        List<OcorrenciaResponseDTO> lista = facade.listarTodas().stream()
                .map(OcorrenciaResponseDTO::fromDomain)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Avançar estado da ocorrência", description = "Executa a transição da máquina de estados (Aberta -> EmAnalise -> Resolvida -> Reaberta).")
    @ApiResponse(responseCode = "200", description = "Estado atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Transição de estado inválida segundo as regras de negócio")
    @PatchMapping("/{protocolo}/status")
    public ResponseEntity<OcorrenciaResponseDTO> avancarStatus(
            @PathVariable UUID protocolo,
            @Valid @RequestBody AvancarStatusRequestDTO dto) {

        StatusOcorrencia novoStatus = switch (dto.novoStatusTipo().toUpperCase()) {
            case "EM_ANALISE" -> new StatusOcorrencia.EmAnalise(LocalDateTime.now(), dto.responsavel() != null ? dto.responsavel() : "Equipe Padrão");
            case "RESOLVIDA" -> new StatusOcorrencia.Resolvida(LocalDateTime.now(), dto.observacaoOuMotivo() != null ? dto.observacaoOuMotivo() : "Serviço Concluído");
            case "REABERTA" -> new StatusOcorrencia.Reaberta(LocalDateTime.now(), dto.observacaoOuMotivo() != null ? dto.observacaoOuMotivo() : "Problema Persiste");
            default -> throw new IllegalArgumentException("Tipo de status inválido: " + dto.novoStatusTipo());
        };

        Ocorrencia atualizada = facade.avancarStatus(protocolo, novoStatus);
        return ResponseEntity.ok(OcorrenciaResponseDTO.fromDomain(atualizada));
    }
}
