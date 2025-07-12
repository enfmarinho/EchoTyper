package br.ufrn.EchoTyper.LLM.service.ContextStrategies;

import com.fasterxml.jackson.databind.JsonNode;

public interface ContextProvider {
    String getContext(JsonNode payload);
}
