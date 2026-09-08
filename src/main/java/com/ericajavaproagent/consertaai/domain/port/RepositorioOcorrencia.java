package com.ericajavaproagent.consertaai.domain.port;

import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 🏛️ PORT (Clean Architecture / Inversão de Dependência):
 * Define as operações de persistência sem saber como elas são implementadas.
 */
public interface RepositorioOcorrencia {

    void salvar(Ocorrencia ocorrencia);

    Optional<Ocorrencia> buscarPorProtocolo(UUID protocolo);

    List<Ocorrencia> listarTodas();
}
