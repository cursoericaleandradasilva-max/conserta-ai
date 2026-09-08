package com.ericajavaproagent.consertaai.domain.port;

import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;

/**
 * 🏛️ PORT: Contrato para notificação do cidadão e equipes públicas.
 */
public interface NotificadorPort {

    void notificarCidadao(Ocorrencia ocorrencia, String mensagem);
}
