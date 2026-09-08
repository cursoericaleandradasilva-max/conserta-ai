package com.ericajavaproagent.consertaai.application.usecase;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;
import com.ericajavaproagent.consertaai.domain.port.NotificadorPort;
import com.ericajavaproagent.consertaai.domain.port.RepositorioOcorrencia;
import com.ericajavaproagent.consertaai.domain.strategy.PriorizacaoStrategyContext;
import org.springframework.stereotype.Service;

/**
 * Caso de Uso: Registrar Ocorrencia.
 * Orquestra o calculo de prioridade via Strategy Pattern, persistencia e notificacao.
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
        Prioridade prioridade = strategyContext.resolverPrioridade(categoria, descricao, latitude, longitude);
        Ocorrencia ocorrencia = Ocorrencia.registrar(categoria, descricao, enderecoOuReferencia, latitude, longitude, prioridade);
        repositorio.salvar(ocorrencia);
        notificador.notificarCidadao(ocorrencia, "Ocorrencia registrada com sucesso. Protocolo: " + ocorrencia.getProtocolo());
        return ocorrencia;
    }
}
