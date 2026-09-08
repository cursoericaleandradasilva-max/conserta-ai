package com.ericajavaproagent.consertaai.application.facade;

import com.ericajavaproagent.consertaai.application.usecase.*;
import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import com.ericajavaproagent.consertaai.domain.model.StatusOcorrencia;
import com.ericajavaproagent.consertaai.domain.port.IaClassifierPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 🏛️ DESIGN PATTERN: Facade Pattern (GoF)
 *
 * Oferece uma interface simplificada e unificada para o subsistema de casos de uso do ConsertaAI.
 */
@Service
public class ConsertaAiFacade {

    private final RegistrarOcorrenciaUseCase registrarUseCase;
    private final ConsultarOcorrenciaUseCase consultarUseCase;
    private final AvancarStatusOcorrenciaUseCase avancarStatusUseCase;
    private final TriagemComIaUseCase triagemIaUseCase;

    public ConsertaAiFacade(RegistrarOcorrenciaUseCase registrarUseCase,
                            ConsultarOcorrenciaUseCase consultarUseCase,
                            AvancarStatusOcorrenciaUseCase avancarStatusUseCase,
                            TriagemComIaUseCase triagemIaUseCase) {
        this.registrarUseCase = registrarUseCase;
        this.consultarUseCase = consultarUseCase;
        this.avancarStatusUseCase = avancarStatusUseCase;
        this.triagemIaUseCase = triagemIaUseCase;
    }

    public Ocorrencia registrarOcorrencia(Categoria categoria, String descricao, String endereco, double lat, double lon) {
        return registrarUseCase.executar(categoria, descricao, endereco, lat, lon);
    }

    public Ocorrencia buscarPorProtocolo(UUID protocolo) {
        return consultarUseCase.buscarPorProtocolo(protocolo);
    }

    public List<Ocorrencia> listarTodas() {
        return consultarUseCase.listarTodas();
    }

    public Ocorrencia avancarStatus(UUID protocolo, StatusOcorrencia novoStatus) {
        return avancarStatusUseCase.executar(protocolo, novoStatus);
    }

    public IaClassifierPort.IaTriagemResult analisarRelatoComIa(String relato) {
        return triagemIaUseCase.analisar(relato);
    }
}
