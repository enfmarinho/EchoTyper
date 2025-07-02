package br.ufrn.EchoTyper.LLM.service.CheckConflictStrategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.meeting.service.MeetingService;

@Service
public class CheckConflictLectureStrategy implements CheckConflictStrategy {

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
            A seguir está a transcrição de uma aula:
            %s

            E abaixo estão os resumos ou transcrições de aulas anteriores sobre o mesmo tema:
            %s

            Analise se há contradições, inconsistências ou abordagens conflitantes entre o conteúdo da aula atual e o conteúdo discutido nas aulas anteriores.
            Se houver, destaque os trechos conflitantes e explique brevemente a contradição.
            Se não houver conflitos, diga que não foram encontradas inconsistências de conteúdo.
            """
            .formatted(transcription, getContext(payload));
    }
}
