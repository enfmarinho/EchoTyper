package br.ufrn.EchoTyper.LLM.service.PromptTemplates;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class SummaryMeeting extends AbstractPromptTemplate {

    @Override
    protected String getIntroduction() {
        return "A seguir está a transcrição de uma reunião:";
    }

    @Override
    protected String getTaskDescription() {
        return "Crie um resumo da reunião, destacando decisões tomadas, tarefas atribuídas, prazos e responsáveis, quando possível.";
    }

    @Override
    protected String getContext(JsonNode payload) {
        if (payload.path("groupId").isMissingNode()) {
            return "";
        }
        // Usa o ContextProvider herdado da classe base
        return "A seguir, há uma lista com resumos de reuniões anteriores relacionadas. Considere esses resumos para identificar continuidade, mudanças de planos ou progresso, e integre essas informações ao resumo da reunião atual, se fizer sentido:\n"
               + groupSummaryContextProvider.getContext(payload);
    }
    
}
