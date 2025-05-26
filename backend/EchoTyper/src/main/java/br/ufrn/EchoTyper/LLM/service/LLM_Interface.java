package br.ufrn.EchoTyper.LLM.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface LLM_Interface {
  public JsonNode checkConflicts(JsonNode payload) throws Exception;
  public JsonNode summarize(JsonNode payload) throws Exception;
}
