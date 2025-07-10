package br.ufrn.EchoTyper.LLM.service.PromptTemplates;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class CheckConflictLecture extends AbstractPromptTemplate {

    @Override
    protected String getIntroduction() {
        return "A seguir está a transcrição de uma aula:";
    }

    @Override
    protected String getTaskDescription() {
        return  """
                Analise se há contradições, inconsistências ou abordagens conflitantes entre o conteúdo da aula atual e o conteúdo discutido nas aulas anteriores.
                Se houver, destaque os trechos conflitantes e explique brevemente a contradição.
                Se não houver conflitos, diga que não foram encontradas inconsistências de conteúdo.
                """;
    }

    @Override
    protected String getContext(JsonNode payload) {
        if (payload.path("groupId").isMissingNode()) {
            return "";
        }
        // Usa o ContextProvider herdado da classe base
        return "Abaixo estão os resumos ou transcrições de aulas anteriores sobre o mesmo tema:\n"
               + groupSummaryContextProvider.getContext(payload);
    }
    
}
