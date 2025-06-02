package br.ufrn.EchoTyper.LLM.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.Json;

import br.ufrn.EchoTyper.meeting.service.MeetingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiApiService implements LLM_Interface {

  @Value("${google.api.key}")
  private String apiKey;

  @Autowired
  private MeetingService meetingService;

  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();
  private String baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

  public JsonNode checkConflicts(JsonNode payload) throws Exception {
    String prompt = buildConflictPrompt(payload);
    return callGeminiApi(prompt);
  }

  public JsonNode summarize(JsonNode payload) throws Exception {
    String prompt = buildSummaryPrompt(payload);
    return callGeminiApi(prompt);
  }

  private JsonNode callGeminiApi(String prompt) throws Exception {
    String requestBody = String.format("""
        {
          "contents": [
            {
              "role": "user",
              "parts": [
                { "text": "%s" }
              ]
            }
          ]
        }
        """, escapeJson(prompt));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
    String url = baseUrl + apiKey;

    ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
    return objectMapper.readTree(response.getBody());
  }

  private String buildConflictPrompt(JsonNode payload) {
    String transcription = payload.path("transcription").asText();
    String events = payload.path("events").toString();

    return """
        A seguir está a transcrição de uma reunião:
        %s

        E abaixo está a lista de eventos já agendados (JSON):
        %s

        Verifique se há conflitos de data entre o que foi dito na reunião e os eventos agendados.
        Se houver conflitos, retorne os IDs dos eventos em conflito e com qual evento na transcrição eles conflitam.
        Se não houver conflitos, diga que não há conflitos.
        """
        .formatted(transcription, events);
  }

  private String buildSummaryPrompt(JsonNode payload) {
    String transcription = payload.path("transcription").asText();
    JsonNode groupIdNode = payload.path("groupId");
    if (groupIdNode.isMissingNode()) {

      return """
          A seguir está a transcrição de uma reunião:
          %s

          Crie um breve resumo da reunião, organizando em tópicos e detalhando apenas os pontos mais relevantes.
          """
          .formatted(transcription);
    }
    return """
        A seguir está a transcrição de uma reunião:
        %s

        Crie um breve resumo da reunião, organizando em tópicos e detalhando apenas os pontos mais relevantes.
        A seguir, ha uma lista com resumos de reunioes relacionadas a atual. Caso esta lista nao esteja vazia, use o contexto destes resumos para complementar
        o resumo gerado para a reuniao atual, quando possivel ou adequado.
        %s
        """
        .formatted(transcription, getSummaryContext(groupIdNode.asLong()));

  }

  private String escapeJson(String input) {
    return input.replace("\"", "\\\"").replace("\n", "\\n");
  }

  private String getSummaryContext(Long id) {
    StringBuilder builder = new StringBuilder("[");
    meetingService.getSummariesByGroup(id).stream()
        .forEach((summary) -> builder.append(String.format("\"%s\"%n", summary)));
    builder.append("]");
    return builder.toString();
  }
}