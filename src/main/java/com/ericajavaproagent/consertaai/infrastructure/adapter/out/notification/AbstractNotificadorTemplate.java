package com.ericajavaproagent.consertaai.infrastructure.adapter.out.notification;

import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import com.ericajavaproagent.consertaai.domain.port.NotificadorPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Template Method Pattern (GoF).
 * Define a estrutura do algoritmo de notificacao.
 */
public abstract class AbstractNotificadorTemplate implements NotificadorPort {

    private static final Logger log = LoggerFactory.getLogger(AbstractNotificadorTemplate.class);

    @Override
    public final void notificarCidadao(Ocorrencia ocorrencia, String mensagem) {
        String payloadFormatado = formatarMensagem(ocorrencia, mensagem);
        executarEnvio(ocorrencia, payloadFormatado);
        auditarNotificacao(ocorrencia.getProtocolo().toString(), payloadFormatado);
    }

    protected String formatarMensagem(Ocorrencia ocorrencia, String mensagem) {
        return String.format("[CONSERTA-AI] Protocolo: %s | Categoria: %s | Mensagem: %s",
                ocorrencia.getProtocolo(), ocorrencia.getCategoria(), mensagem);
    }

    protected abstract void executarEnvio(Ocorrencia ocorrencia, String payload);

    protected void auditarNotificacao(String id, String payload) {
        log.info("Auditoria de Notificacao disparada para o protocolo {}", id);
    }
}
