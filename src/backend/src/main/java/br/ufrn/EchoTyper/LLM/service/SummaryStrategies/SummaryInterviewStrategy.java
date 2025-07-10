package br.ufrn.EchoTyper.LLM.service.SummaryStrategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.meeting.service.MeetingService;

@Service
public class SummaryInterviewStrategy implements SummaryStrategy {

    @Autowired
    private MeetingService meetingService;

    public String getContext(long id) {
        StringBuilder builder = new StringBuilder("[");

        meetingService.getGroupContext(id).stream()
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
                A seguir está a transcrição de uma entrevista:
                %s

                Crie um resumo da entrevista organizado por tópicos. Destaque as principais perguntas feitas, as respostas mais significativas do entrevistado, temas abordados, e quaisquer insights, opiniões ou experiências marcantes que surgiram na conversa.
                """
                .formatted(transcription);
        }
        return 
            """
            A seguir está a transcrição de uma entrevista com um candidato:
            %s

            Crie um resumo da entrevista, estruturado por tópicos. Destaque os pontos principais da conversa: perguntas feitas, respostas significativas, experiências mencionadas, competências demonstradas e quaisquer opiniões ou insights relevantes.

            A seguir, há resumos de entrevistas anteriores com outros candidatos para a mesma posição ou contexto:
            %s

            Com base nisso, compare o desempenho, perfil e respostas deste candidato com os anteriores. Aponte semelhanças, diferenças e destaques, e indique aspectos que possam torná-lo mais ou menos adequado à vaga ou objetivo da entrevista.
            """
            .formatted(transcription, getContext(groupIdNode.asLong()));

    }
}
