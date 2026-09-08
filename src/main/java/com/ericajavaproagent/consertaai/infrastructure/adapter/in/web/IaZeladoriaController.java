package com.ericajavaproagent.consertaai.infrastructure.adapter.in.web;

import com.ericajavaproagent.consertaai.application.facade.ConsertaAiFacade;
import com.ericajavaproagent.consertaai.domain.port.IaClassifierPort;
import com.ericajavaproagent.consertaai.infrastructure.adapter.in.web.dto.IaTriagemRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller RESTful de Inteligencia Artificial.
 */
@RestController
@RequestMapping("/api/v1/triagens-ia")
@Tag(name = "Triagens IA", description = "Classificacao automatica de relatos com Inteligencia Artificial")
public class IaZeladoriaController {

    private final ConsertaAiFacade facade;

    public IaZeladoriaController(ConsertaAiFacade facade) {
        this.facade = facade;
    }

    @Operation(summary = "Executar triagem automatica com IA", description = "Recebe um relato e retorna a categoria e prioridade sugeridas.")
    @PostMapping
    public ResponseEntity<IaClassifierPort.IaTriagemResult> triagemComIa(@Valid @RequestBody IaTriagemRequestDTO dto) {
        IaClassifierPort.IaTriagemResult resultado = facade.analisarRelatoComIa(dto.relato());
        return ResponseEntity.ok(resultado);
    }
}
