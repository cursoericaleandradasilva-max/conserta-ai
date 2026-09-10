package com.ericajavaproagent.consertaai.application.usecase;

import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import com.ericajavaproagent.consertaai.domain.port.RepositorioOcorrencia;
import org.springframework.stereotype.Service;

@Service
public class RegistrarOcorrenciaUseCase {

    private final RepositorioOcorrencia repositorioOcorrencia;

    public RegistrarOcorrenciaUseCase(RepositorioOcorrencia repositorioOcorrencia) {
        this.repositorioOcorrencia = repositorioOcorrencia;
    }

    public Ocorrencia executar(Ocorrencia ocorrencia) {
        repositorioOcorrencia.salvar(ocorrencia);
        return ocorrencia;
    }
}