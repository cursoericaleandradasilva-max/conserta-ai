package com.ericajavaproagent.consertaai.application.usecase;

import com.ericajavaproagent.consertaai.domain.port.IaClassifierPort;
import org.springframework.stereotype.Service;

@Service
public class TriagemComIaUseCase {

    private final IaClassifierPort iaClassifier;

    public TriagemComIaUseCase(IaClassifierPort iaClassifier) {
        this.iaClassifier = iaClassifier;
    }

    public IaClassifierPort.IaTriagemResult analisar(String relatoCidadao) {
        return iaClassifier.analisarRelato(relatoCidadao);
    }
}
