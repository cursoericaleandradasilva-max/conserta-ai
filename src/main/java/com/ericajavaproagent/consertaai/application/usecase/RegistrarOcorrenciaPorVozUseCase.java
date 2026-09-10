package com.ericajavaproagent.consertaai.application.usecase;

import com.ericajavaproagent.consertaai.application.facade.ConsertaAiFacade;
import com.ericajavaproagent.consertaai.domain.port.AudioToTextPort;
import org.springframework.stereotype.Service;

@Service
public class RegistrarOcorrenciaPorVozUseCase {

    private final AudioToTextPort audioToTextPort;
    private final ConsertaAiFacade consertaAiFacade;

    public RegistrarOcorrenciaPorVozUseCase(AudioToTextPort audioToTextPort, ConsertaAiFacade consertaAiFacade) {
        this.audioToTextPort = audioToTextPort;
        this.consertaAiFacade = consertaAiFacade;
    }

    public void registrarViaAudio(byte[] arquivoAudio, String nomeArquivo) {
        // 1. Transcreve o áudio para texto usando o Whisper
        String relatoTranscrito = audioToTextPort.transcreverAudio(arquivoAudio, nomeArquivo);

        // 2. Se o Facade espera um DTO, você pode instanciá-lo aqui ou ajustar conforme o método existente:
        // Exemplo: consertaAiFacade.registrarOcorrencia(new OcorrenciaRequestDto(relatoTranscrito));
    }
}