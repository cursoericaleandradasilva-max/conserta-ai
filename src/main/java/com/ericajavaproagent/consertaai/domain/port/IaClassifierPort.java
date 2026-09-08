package com.ericajavaproagent.consertaai.domain.port;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;

/**
 * 🏛️ PORT: Contrato para integração com o modelo de Inteligência Artificial.
 */
public interface IaClassifierPort {

    record IaTriagemResult(Categoria categoriaDetectada, Prioridade prioridadeSugerida, String justificativaIa) {}

    IaTriagemResult analisarRelato(String relatoCidadao);
}
