package com.ericajavaproagent.consertaai.application.usecase;

import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import com.ericajavaproagent.consertaai.domain.model.StatusOcorrencia;
import com.ericajavaproagent.consertaai.domain.port.NotificadorPort;
import com.ericajavaproagent.consertaai.domain.port.RepositorioOcorrencia;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AvancarStatusOcorrenciaUseCase {

    private final RepositorioOcorrencia repositorio;
    private final NotificadorPort notificador;

    public AvancarStatusOcorrenciaUseCase(RepositorioOcorrencia repositorio, NotificadorPort notificador) {
        this.repositorio = repositorio;
        this.notificador = notificador;
    }

    public Ocorrencia executar(UUID protocolo, StatusOcorrencia novoStatus) {
        Ocorrencia ocorrencia = repositorio.buscarPorProtocolo(protocolo)
                .orElseThrow(() -> new IllegalArgumentException("Ocorrência não encontrada para o protocolo: " + protocolo));

        // A entidade rica valida a transição de estado (State Pattern)
        ocorrencia.avancarPara(novoStatus);

        repositorio.salvar(ocorrencia);

        notificador.notificarCidadao(ocorrencia, "Status da ocorrência atualizado para: " + novoStatus.getClass().getSimpleName());

        return ocorrencia;
    }
}
