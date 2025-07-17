package br.ufrn.EchoTyper.LLM.service.PromptTemplates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.LLM.service.ContextStrategies.ContextProvider;

@Service
public class SummaryInterview extends AbstractPromptTemplate {

    @Autowired
    protected ContextProvider groupSummaryContextProvider;

    @Override
    protected String getIntroduction() {
        return "A seguir está a transcrição de uma entrevista com um candidato junto de informações sobre o processo seletivo e entrevista:";
    }

    @Override
    protected String getTaskDescription() {
        return "Crie um resumo da entrevista, estruturado por tópicos. Destaque os pontos principais da conversa: perguntas feitas, respostas significativas, experiências mencionadas, competências demonstradas e quaisquer opiniões ou insights relevantes.";
    }

    @Override
    protected String getContext(JsonNode payload) {
        if (payload.path("groupId").isMissingNode()) {
            return "";
        }
        // Usa o ContextProvider herdado da classe base
        return "A seguir, há resumos e informações de entrevistas anteriores com outros candidatos para a mesma posição ou contexto:\n"
               + groupSummaryContextProvider.getContext(payload)
               + "\n\nCom base nisso, compare o desempenho, perfil e respostas deste candidato com os anteriores. Aponte semelhanças, diferenças e destaques, e indique aspectos que possam torná-lo mais ou menos adequado à vaga ou objetivo da entrevista.";
    }
    
}
