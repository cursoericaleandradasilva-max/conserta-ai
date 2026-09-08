package com.ericajavaproagent.consertaai.domain.port;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;

/**
 * Output Port para o servico de classificacao e triagem por Inteligencia Artificial.
 */
public interface IaClassifierPort {

    record IaTriagemResult(Categoria categoriaDetectada, Prioridade prioridadeSugerida, String justificativaIa) {}

    IaTriagemResult analisarRelato(String relatoCidadao);
}
