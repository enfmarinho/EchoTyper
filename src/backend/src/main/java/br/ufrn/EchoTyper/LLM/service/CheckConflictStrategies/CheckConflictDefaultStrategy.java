package br.ufrn.EchoTyper.LLM.service.CheckConflictStrategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import br.ufrn.EchoTyper.calendar.service.CalendarService;

@Service
public class CheckConflictDefaultStrategy implements CheckConflictStrategy {
    @Autowired
    private CalendarService calendarService;

    public String getContext(JsonNode payload) {
        String events = calendarService.getAllEvents().toString();
        return events;
    }

    public String buildConflictPrompt(JsonNode payload) {
        String transcription = payload.path("transcription").asText();

        return 
            """
            Você é um assistente especializado em verificar conflitos de agenda.
            Sua tarefa é analisar a transcrição de uma reunião e verificar se há conflitos com eventos já agendados.
            A seguir está a transcrição de uma reunião:
            %s

            E abaixo está a lista de eventos já agendados (JSON):
            %s

            Verifique se há conflitos de datas entre compromissos ou eventos mencionados na reunião e os eventos agendados.
            Se houver conflitos, retorne os IDs dos eventos agendados em conflito e com qual evento mencionado na transcrição eles conflitam.
            Se não houver conflitos, diga claramente que não há conflitos.
            """
            .formatted(transcription, getContext(payload));
    }
}
