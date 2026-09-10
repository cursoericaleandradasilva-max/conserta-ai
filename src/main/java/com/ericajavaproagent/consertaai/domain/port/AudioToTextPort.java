package com.ericajavaproagent.consertaai.domain.port;

public interface AudioToTextPort {
    String transcreverAudio(byte[] arquivoAudio, String nomeArquivo);
}