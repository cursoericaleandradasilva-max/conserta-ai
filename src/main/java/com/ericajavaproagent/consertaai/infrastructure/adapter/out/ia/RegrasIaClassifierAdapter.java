package com.ericajavaproagent.consertaai.infrastructure.adapter.out.ia;

import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;
import com.ericajavaproagent.consertaai.domain.port.IaClassifierPort;
import org.springframework.stereotype.Component;

/**
 * 🤖 ADAPTADOR DE INTELIGÊNCIA ARTIFICIAL:
 * Analisa relatos livres de cidadãos e sugere Categoria e Prioridade de zeladoria.
 */
@Component
public class RegrasIaClassifierAdapter implements IaClassifierPort {

    @Override
    public IaTriagemResult analisarRelato(String relatoCidadao) {
        if (relatoCidadao == null || relatoCidadao.trim().isEmpty()) {
            return new IaTriagemResult(Categoria.OUTRO, Prioridade.BAIXA, "Relato em branco ou insuficiente.");
        }

        String texto = relatoCidadao.toLowerCase();

        // 1. Detecção de Buraco na Via
        if (texto.contains("buraco") || texto.contains("cratera") || texto.contains("asfalto") || texto.contains("pista") || texto.contains("pavimento")) {
            Prioridade p = (texto.contains("acidente") || texto.contains("avenida") || texto.contains("profundo"))
                    ? Prioridade.CRITICA : Prioridade.ALTA;
            return new IaTriagemResult(Categoria.BURACO_VIA, p,
                    "A IA identificou termos relacionados a danos na malha viária com risco ao tráfego.");
        }

        // 2. Detecção de Iluminação Pública
        if (texto.contains("luz") || texto.contains("poste") || texto.contains("escuro") || texto.contains("lampada") || texto.contains("iluminacao")) {
            Prioridade p = (texto.contains("hospital") || texto.contains("escola") || texto.contains("perigo"))
                    ? Prioridade.ALTA : Prioridade.MEDIA;
            return new IaTriagemResult(Categoria.ILUMINACAO_PUBLICA, p,
                    "A IA detectou falha na infraestrutura elétrica/iluminação pública.");
        }

        // 3. Detecção de Lixo Acumulado
        if (texto.contains("lixo") || texto.contains("entulho") || texto.contains("bueiro") || texto.contains("sujeira") || texto.contains("descarte")) {
            Prioridade p = (texto.contains("dengue") || texto.contains("rato") || texto.contains("enchente"))
                    ? Prioridade.CRITICA : Prioridade.MEDIA;
            return new IaTriagemResult(Categoria.LIXO_ACUMULADO, p,
                    "A IA detectou acúmulo de resíduos com potencial risco sanitário ou de drenagem.");
        }

        return new IaTriagemResult(Categoria.OUTRO, Prioridade.BAIXA,
                "A IA classificou a ocorrência na categoria geral para análise humana.");
    }
}
