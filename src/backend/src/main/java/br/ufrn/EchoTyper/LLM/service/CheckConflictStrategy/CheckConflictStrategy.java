package br.ufrn.EchoTyper.LLM.service.CheckConflictStrategy;

import com.fasterxml.jackson.databind.JsonNode;

public interface CheckConflictStrategy {
    String buildConflictPrompt(JsonNode payload);
    String getContext(JsonNode payload);
}
