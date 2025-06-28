package br.ufrn.EchoTyper.LLM.service.SummaryStrategy;

import com.fasterxml.jackson.databind.JsonNode;

public interface SummaryStrategy {
    String buildSummaryPrompt(JsonNode payload);
    String getContext(long groupId);
}
