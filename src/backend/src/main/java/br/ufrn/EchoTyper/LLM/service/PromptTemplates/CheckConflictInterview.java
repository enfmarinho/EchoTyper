package br.ufrn.EchoTyper.LLM.service.PromptTemplates;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class CheckConflictInterview extends AbstractPromptTemplate {

    @Override
    protected String getIntroduction() {
        return "A seguir está a transcrição de uma entrevista com um candidato:";
    }

    @Override
    protected String getTaskDescription() {
        return  """
                Verifique se há contradições entre declarações feitas nesta entrevista e entrevistas anteriores, tanto no conteúdo das respostas quanto nas informações fornecidas (ex: experiência, disponibilidade, competências, etc).
                Se houver conflitos ou inconsistências, destaque as declarações conflitantes e explique por que elas entram em contradição.
                Se não houver, diga que não foram encontradas contradições nas declarações.
                """;
    }

    @Override
    protected String getContext(JsonNode payload) {
        if (payload.path("groupId").isMissingNode()) {
            return "";
        }
        // Usa o ContextProvider herdado da classe base
        return "Abaixo estão os resumos ou transcrições de entrevistas anteriores com o mesmo candidato ou outros candidatos para a mesma posição:\n"
               + groupSummaryContextProvider.getContext(payload);
    }
    
}
