package com.ericajavaproagent.consertaai.domain.port;

import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output Port para persistencia de ocorrencias (Clean Architecture).
 */
public interface RepositorioOcorrencia {

    void salvar(Ocorrencia ocorrencia);

    Optional<Ocorrencia> buscarPorProtocolo(UUID protocolo);

    List<Ocorrencia> listarTodas();
}
