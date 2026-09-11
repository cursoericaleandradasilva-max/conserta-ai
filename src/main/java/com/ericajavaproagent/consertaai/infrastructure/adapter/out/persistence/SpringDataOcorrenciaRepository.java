package com.ericajavaproagent.consertaai.infrastructure.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataOcorrenciaRepository extends JpaRepository<OcorrenciaJpaEntity, UUID> {
}