package br.ufrn.EchoTyper.LLM.service.PromptTemplates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.LLM.service.ContextStrategies.ContextProvider;

@Service
public class CheckConflictMeeting extends AbstractPromptTemplate {

    @Autowired
    @Qualifier("calendarContextProvider")
    protected ContextProvider calendarContextProvider;

    @Override
    protected String getIntroduction() {
        return "Você é um assistente especializado em verificar conflitos de agenda. A seguir está a transcrição de uma reunião:";
    }

    @Override
    protected String getTaskDescription() {
        return "Sua tarefa é analisar a transcrição de uma reunião e verificar se há conflitos com eventos já agendados.";
    }

    @Override
    protected String getContext(JsonNode payload) {
        // Usa o ContextProvider herdado da classe base
        return "A seguir, está a lista de eventos já agendados (JSON)::\n"
               + calendarContextProvider.getContext(payload)
               + """
                Verifique se há conflitos de datas entre compromissos ou eventos mencionados na reunião e os eventos agendados.
                Se houver conflitos, retorne os IDs dos eventos agendados em conflito e com qual evento mencionado na transcrição eles conflitam.
                Se não houver conflitos, diga claramente que não há conflitos.""";
    }
    
}
