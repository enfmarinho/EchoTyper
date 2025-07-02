package br.ufrn.EchoTyper.LLM.service.SummaryStrategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.meeting.service.MeetingService;

@Service
public class SummaryLectureStrategy implements SummaryStrategy {

    @Autowired
    private MeetingService meetingService;

    public String getContext(long id) {
        StringBuilder builder = new StringBuilder("[");

        meetingService.getSummariesByGroup(id).stream()
            .forEach((summary) -> builder.append(String.format("\"%s\"%n", summary)));
            
        builder.append("]");
        return builder.toString();
    }

    public String buildSummaryPrompt(JsonNode payload) {

        String transcription = payload.path("transcription").asText();
        JsonNode groupIdNode = payload.path("groupId");

        if (groupIdNode.isMissingNode()) {

            return 
                """
                A seguir está a transcrição de uma aula:
                %s

                Crie um resumo organizado por tópicos. Foque nos conceitos explicados, exemplos dados, perguntas respondidas e conclusões importantes. Mantenha o conteúdo claro e didático, como se fosse um material de revisão.
                """
                .formatted(transcription);
        }
        return 
            """
            A seguir está a transcrição de uma aula:
            %s

            Crie um resumo didático da aula, organizado por tópicos e com foco em conteúdo. Priorize os conceitos ensinados, explicações relevantes, dúvidas resolvidas e conclusões.
            A seguir, há resumos de aulas anteriores sobre o mesmo tema. Use essas informações para indicar continuidade de conteúdo ou reforçar ligações entre os assuntos:
            %s
            """
            .formatted(transcription, getContext(groupIdNode.asLong()));

    }
    
}
