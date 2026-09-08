package com.ericajavaproagent.consertaai.application.usecase;

import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import com.ericajavaproagent.consertaai.domain.port.RepositorioOcorrencia;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConsultarOcorrenciaUseCase {

    private final RepositorioOcorrencia repositorio;

    public ConsultarOcorrenciaUseCase(RepositorioOcorrencia repositorio) {
        this.repositorio = repositorio;
    }

    public Ocorrencia buscarPorProtocolo(UUID protocolo) {
        return repositorio.buscarPorProtocolo(protocolo)
                .orElseThrow(() -> new IllegalArgumentException("Ocorrência não encontrada para o protocolo: " + protocolo));
    }

    public List<Ocorrencia> listarTodas() {
        return repositorio.listarTodas();
    }
}
