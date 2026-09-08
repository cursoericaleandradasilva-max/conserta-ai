package com.ericajavaproagent.consertaai.domain.port;

import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;

/**
 * Output Port para notificacao de eventos.
 */
public interface NotificadorPort {

    void notificarCidadao(Ocorrencia ocorrencia, String mensagem);
}
