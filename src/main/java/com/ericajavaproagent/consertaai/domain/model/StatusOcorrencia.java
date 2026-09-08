package com.ericajavaproagent.consertaai.domain.model;

import java.time.LocalDateTime;

/**
 * State Pattern com Sealed Interface (Java 21).
 * Representa os estados da Ocorrencia garantindo tipagem estrita e imutabilidade.
 */
public sealed interface StatusOcorrencia
        permits StatusOcorrencia.Aberta,
                StatusOcorrencia.EmAnalise,
                StatusOcorrencia.Resolvida,
                StatusOcorrencia.Reaberta {

    record Aberta(LocalDateTime criadoEm) implements StatusOcorrencia {}

    record EmAnalise(LocalDateTime iniciadoEm, String responsavel) implements StatusOcorrencia {}

    record Resolvida(LocalDateTime resolvidoEm, String observacao) implements StatusOcorrencia {}

    record Reaberta(LocalDateTime reabertoEm, String motivo) implements StatusOcorrencia {}
}
