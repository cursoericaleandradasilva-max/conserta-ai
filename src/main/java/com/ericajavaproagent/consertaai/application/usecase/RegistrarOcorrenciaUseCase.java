package com.ericajavaproagent.consertaai.application.usecase;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;
import com.ericajavaproagent.consertaai.domain.port.NotificadorPort;
import com.ericajavaproagent.consertaai.domain.port.RepositorioOcorrencia;
import com.ericajavaproagent.consertaai.domain.strategy.PriorizacaoStrategyContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * ⚙️ CASO DE USO: Registrar Ocorrência
 *
 * Orquestra o cálculo de prioridade (Strategy Pattern), persistência (Port) e notificação.
 */
@Service
public class RegistrarOcorrenciaUseCase {

    private final RepositorioOcorrencia repositorio;
    private final PriorizacaoStrategyContext strategyContext;
    private final NotificadorPort notificador;

    public RegistrarOcorrenciaUseCase(RepositorioOcorrencia repositorio,
                                      PriorizacaoStrategyContext strategyContext,
                                      NotificadorPort notificador) {
        this.repositorio = repositorio;
        this.strategyContext = strategyContext;
        this.notificador = notificador;
    }

    public Ocorrencia executar(Categoria categoria, String descricao, String enderecoOuReferencia,
                               double latitude, double longitude) {
        // 1. Resolve prioridade através do Strategy Pattern
        Prioridade prioridade = strategyContext.resolverPrioridade(categoria, descricao, latitude, longitude);

        // 2. Cria a ocorrência através da Factory da entidade
        Ocorrencia ocorrencia = Ocorrencia.registrar(categoria, descricao, enderecoOuReferencia, latitude, longitude, prioridade);

        // 3. Salva no repositório desacoplado
        repositorio.salvar(ocorrencia);

        // 4. Notifica o cidadão sobre o protocolo gerado
        notificador.notificarCidadao(ocorrencia, "Ocorrência registrada com sucesso! Protocolo: " + ocorrencia.getProtocolo());

        return ocorrencia;
    }
}
