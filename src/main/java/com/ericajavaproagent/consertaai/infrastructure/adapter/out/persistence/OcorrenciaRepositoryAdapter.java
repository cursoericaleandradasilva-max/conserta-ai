package com.ericajavaproagent.consertaai.infrastructure.adapter.out.persistence;

import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import com.ericajavaproagent.consertaai.domain.port.RepositorioOcorrencia;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OcorrenciaRepositoryAdapter implements RepositorioOcorrencia {

    private final SpringDataOcorrenciaRepository springDataRepository;
    private final OcorrenciaMapper mapper;

    public OcorrenciaRepositoryAdapter(SpringDataOcorrenciaRepository springDataRepository, OcorrenciaMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public void salvar(Ocorrencia ocorrencia) {
        OcorrenciaJpaEntity entity = mapper.toJpa(ocorrencia);
        springDataRepository.save(entity);
    }

    @Override
    public Optional<Ocorrencia> buscarPorProtocolo(UUID protocolo) {
        return springDataRepository.findById(protocolo)
                .map(mapper::toDomain);
    }

    @Override
    public List<Ocorrencia> listarTodas() {
        return springDataRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
}