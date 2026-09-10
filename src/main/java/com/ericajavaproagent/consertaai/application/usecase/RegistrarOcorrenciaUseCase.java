package com.ericajavaproagent.consertaai.application.usecase;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;
import com.ericajavaproagent.consertaai.domain.port.NotificadorPort;
import com.ericajavaproagent.consertaai.domain.port.RepositorioOcorrencia;
import com.ericajavaproagent.consertaai.domain.strategy.PriorizacaoStrategyContext;
import org.springframework.stereotype.Service;

/**
 * Caso de Uso: Registrar Ocorrência.
 * Orquestra cálculo de prioridade (Strategy), criação do modelo de domínio,
 * persistência e envio de notificações.
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

    public Ocorrencia executar(Categoria categoria, String descricao, String enderecoOuReferencia, double latitude, double longitude) {
        // 1. Calcula a prioridade dinamicamente via Strategy
        Prioridade prioridade = strategyContext.resolverPrioridade(categoria, descricao, latitude, longitude);

        // 2. Instancia o objeto de domínio via Factory Method
        Ocorrencia ocorrencia = Ocorrencia.registrar(categoria, descricao, enderecoOuReferencia, latitude, longitude, prioridade);

        // 3. Persiste no repositório
        repositorio.salvar(ocorrencia);

        // 4. Notifica o cidadão
        notificador.notificarCidadao(ocorrencia, "Sua ocorrência foi registrada com sucesso.");

        return ocorrencia;
    }

    public Ocorrencia executar(Ocorrencia ocorrencia) {
        repositorio.salvar(ocorrencia);
        return ocorrencia;
    }
}