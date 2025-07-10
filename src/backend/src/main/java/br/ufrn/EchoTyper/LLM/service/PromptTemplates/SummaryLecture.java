package br.ufrn.EchoTyper.LLM.service.PromptTemplates;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class SummaryLecture extends AbstractPromptTemplate {

    @Override
    protected String getIntroduction() {
        return "A seguir está a transcrição de uma aula:";
    }

    @Override
    protected String getTaskDescription() {
        return "Crie um resumo organizado por tópicos. Foque nos conceitos explicados, exemplos dados, perguntas respondidas e conclusões importantes. Mantenha o conteúdo claro e didático, como se fosse um material de revisão.";
    }

    @Override
    protected String getContext(JsonNode payload) {
        if (payload.path("groupId").isMissingNode()) {
            return "";
        }
        // Usa o ContextProvider herdado da classe base
        return "A seguir, há resumos de aulas anteriores sobre o mesmo tema. Use essas informações para indicar continuidade de conteúdo ou reforçar ligações entre os assuntos:\n"
               + groupSummaryContextProvider.getContext(payload);
    }
    
}
