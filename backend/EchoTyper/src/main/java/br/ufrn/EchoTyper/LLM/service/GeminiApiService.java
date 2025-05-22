package br.ufrn.EchoTyper.LLM.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiApiService {

    @Value("${google.api.key}")
    private String apiKey;

    public JsonNode checkConflicts(JsonNode payload) throws Exception {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

        String transcription = payload.path("transcription").asText();
        JsonNode events = payload.path("events");

        // Estruturando o prompt com contexto
        StringBuilder structuredPrompt = new StringBuilder();
        structuredPrompt.append("A seguir está a transcrição de uma reunião:\n")
                        .append(transcription)
                        .append("\n\nE abaixo está a lista de eventos já agendados (JSON):\n")
                        .append(events.toString())
                        .append("\n\nVerifique se há conflitos de data entre o que foi dito na reunião e os eventos agendados. ")
                        .append("Se houver conflitos, retorne os IDs dos eventos em conflito e com qual evento na transcrição eles conflitam. ")
                        .append("Se não houver conflitos, diga que não há conflitos.");

        String requestBody = "{ \"contents\": [ { \"role\": \"user\", \"parts\": [ { \"text\": \"" + structuredPrompt.toString().replace("\"", "\\\"") + "\" } ] } ] }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return new ObjectMapper().readTree(response.getBody());
    }
}
