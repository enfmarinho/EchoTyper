package br.ufrn.EchoTyper.LLM.service.CheckConflictStrategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.meeting.service.MeetingService;

@Service
public class CheckConflictInterviewStrategy implements CheckConflictStrategy {

    @Autowired
    private MeetingService meetingService;

    public String getContext(JsonNode payload) {
        JsonNode groupIdNode = payload.path("groupId");
        StringBuilder builder = new StringBuilder("[");

        meetingService.getSummariesByGroup(groupIdNode.asLong()).stream()
            .forEach((summary) -> builder.append(String.format("\"%s\"%n", summary)));
            
        builder.append("]");
        return builder.toString();
    }

    public String buildConflictPrompt(JsonNode payload) {
        String transcription = payload.path("transcription").asText();

        return 
            """
            A seguir está a transcrição de uma entrevista com um candidato:
            %s

            E abaixo estão os resumos ou transcrições de entrevistas anteriores com o mesmo candidato ou outros candidatos para a mesma posição:
            %s

            Verifique se há contradições entre declarações feitas nesta entrevista e entrevistas anteriores, tanto no conteúdo das respostas quanto nas informações fornecidas (ex: experiência, disponibilidade, competências, etc).
            Se houver conflitos ou inconsistências, destaque as declarações conflitantes e explique por que elas entram em contradição.
            Se não houver, diga que não foram encontradas contradições nas declarações.
            """
            .formatted(transcription, getContext(payload));
    }
}
