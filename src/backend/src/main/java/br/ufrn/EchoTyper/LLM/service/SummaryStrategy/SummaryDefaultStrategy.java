package br.ufrn.EchoTyper.LLM.service.SummaryStrategy;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.meeting.service.MeetingService;

public class SummaryDefaultStrategy implements SummaryStrategy {

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
                A seguir está a transcrição de uma reunião:
                %s
                Crie um breve resumo da reunião, organizando em tópicos. Foque nos pontos mais relevantes discutidos, incluindo decisões tomadas, ações acordadas, prazos mencionados e responsáveis designados, se houver.
                """
                .formatted(transcription);
        }
        return 
            """
            A seguir está a transcrição de uma reunião:
            %s

            Crie um resumo da reunião, destacando decisões tomadas, tarefas atribuídas, prazos e responsáveis, quando possível.
            A seguir, há uma lista com resumos de reuniões anteriores relacionadas. Considere esses resumos para identificar continuidade, mudanças de planos ou progresso, e integre essas informações ao resumo da reunião atual, se fizer sentido:
            %s
            """
            .formatted(transcription, getContext(groupIdNode.asLong()));
    }
    
}
