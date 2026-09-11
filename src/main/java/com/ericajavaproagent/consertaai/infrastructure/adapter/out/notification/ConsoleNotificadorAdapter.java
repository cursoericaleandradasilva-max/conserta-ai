package com.ericajavaproagent.consertaai.infrastructure.adapter.out.notification;

import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Adaptador de envio de notificacao via console.
 */
@Component
public class ConsoleNotificadorAdapter extends AbstractNotificadorTemplate {

    private static final Logger log = LoggerFactory.getLogger(ConsoleNotificadorAdapter.class);

    @Override
    protected void executarEnvio(Ocorrencia ocorrencia, String payload) {
        log.info("Notificacao ao Cidadao enviada: {}", payload);
    }
}
