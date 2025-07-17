package br.ufrn.EchoTyper.LLM.service.PromptTemplates;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class AbstractPromptTemplate {

    public final String buildPrompt(JsonNode payload) {

        String mainContent = getMainContent(payload);
        String context = getContext(payload);
        String introduction = getIntroduction();
        String taskDescription = getTaskDescription();

        return String.format("%s\n\n%s\n\n%s\n\n%s", introduction, mainContent, taskDescription, context).trim();
    }

    // --- Subclasses devem implementar ---
    protected abstract String getIntroduction();

    protected abstract String getTaskDescription();

    // --- Subclasses PODEM dar um override ---
    protected String getMainContent(JsonNode payload) {
        return payload.path("transcription").asText();
    }

    protected String getContext(JsonNode payload) {
        return ""; // Default: no context.
    }
}
