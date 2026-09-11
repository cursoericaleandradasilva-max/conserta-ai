package com.ericajavaproagent.consertaai.infrastructure.adapter.out.ia;

import com.ericajavaproagent.consertaai.domain.port.AudioToTextPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class WhisperAudioToTextAdapter implements AudioToTextPort {

    private final RestClient restClient;
    private final String openAiApiKey;

    public WhisperAudioToTextAdapter(
            RestClient.Builder restClientBuilder,
            @Value("${openai.api.key}") String openAiApiKey) {

        this.restClient = restClientBuilder.baseUrl("https://api.openai.com/v1").build();
        this.openAiApiKey = openAiApiKey;
    }

    @Override
    public String transcreverAudio(byte[] arquivoAudio, String nomeArquivo) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("file", new ByteArrayResource(arquivoAudio) {
            @Override
            public String getFilename() {
                return nomeArquivo; // Obrigatório para a API reconhecer o arquivo
            }
        });
        body.add("model", "whisper-1");
        body.add("language", "pt");

        WhisperResponse response = restClient.post()
                .uri("/audio/transcriptions")
                .header("Authorization", "Bearer " + openAiApiKey)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(WhisperResponse.class);

        return response != null ? response.text() : "";
    }

    private record WhisperResponse(String text) {}
}