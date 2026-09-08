package com.ericajavaproagent.consertaai.application;

import com.ericajavaproagent.consertaai.application.usecase.RegistrarOcorrenciaUseCase;
import com.ericajavaproagent.consertaai.domain.model.Categoria;
import com.ericajavaproagent.consertaai.domain.model.Ocorrencia;
import com.ericajavaproagent.consertaai.domain.model.Prioridade;
import com.ericajavaproagent.consertaai.domain.port.NotificadorPort;
import com.ericajavaproagent.consertaai.domain.port.RepositorioOcorrencia;
import com.ericajavaproagent.consertaai.domain.strategy.PriorizacaoStrategyContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrarOcorrenciaUseCaseTest {

    @Mock
    private RepositorioOcorrencia repositorio;

    @Mock
    private PriorizacaoStrategyContext strategyContext;

    @Mock
    private NotificadorPort notificador;

    @InjectMocks
    private RegistrarOcorrenciaUseCase useCase;

    @Test
    @DisplayName("Deve registrar ocorrência calculando prioridade e notificando o cidadão")
    void deveRegistrarOcorrenciaComSucesso() {
        given(strategyContext.resolverPrioridade(any(), anyString(), anyDouble(), anyDouble()))
                .willReturn(Prioridade.CRITICA);

        Ocorrencia ocorrencia = useCase.executar(
                Categoria.BURACO_VIA, "Cratera na via expressa", "Rua Teste, 1", -19.92, -43.94);

        assertThat(ocorrencia).isNotNull();
        assertThat(ocorrencia.getProtocolo()).isNotNull();
        assertThat(ocorrencia.getPrioridade()).isEqualTo(Prioridade.CRITICA);

        ArgumentCaptor<Ocorrencia> captor = ArgumentCaptor.forClass(Ocorrencia.class);
        verify(repositorio).salvar(captor.capture());
        verify(notificador).notificarCidadao(any(), anyString());

        Ocorrencia salva = captor.getValue();
        assertThat(salva.getProtocolo()).isEqualTo(ocorrencia.getProtocolo());
    }
}
