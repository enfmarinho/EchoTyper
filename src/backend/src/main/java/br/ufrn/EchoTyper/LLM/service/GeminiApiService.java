package br.ufrn.EchoTyper.LLM.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufrn.EchoTyper.LLM.service.PromptTemplates.AbstractPromptTemplate;
import br.ufrn.EchoTyper.register.service.RegisterService;
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
  private AbstractPromptTemplate summaryTemplate;

  @Autowired
  private AbstractPromptTemplate checkConflictTemplate;

  private RegisterService registerService;

  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();
  private String baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

  public JsonNode checkConflicts(JsonNode payload) throws Exception {
    if (payload == null || payload.isEmpty()) {
      throw new IllegalArgumentException("Payload cannot be null or empty");
    }

    try {
      String prompt = checkConflictTemplate.buildPrompt(payload);
      return callGeminiApi(prompt);
    } catch (Exception e) {
      throw new Exception("Error when trying to check conflicts: " + e.getMessage(), e);
    }
  }

  public JsonNode summarize(JsonNode payload) throws Exception {
    if (payload == null || payload.isEmpty()) {
      throw new IllegalArgumentException("Payload cannot be null or empty");
    }

    try {
      String prompt = summaryTemplate.buildPrompt(payload);
      return callGeminiApi(prompt);

    } catch (Exception e) {
      throw new Exception("Error when trying to summarize: " + e.getMessage(), e);
    }
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

  private String escapeJson(String input) {
    return input.replace("\"", "\\\"").replace("\n", "\\n");
  }
}