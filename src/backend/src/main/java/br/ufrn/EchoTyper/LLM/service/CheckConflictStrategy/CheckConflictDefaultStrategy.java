package br.ufrn.EchoTyper.LLM.service.CheckConflictStrategy;
import java.util.Calendar;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.json.Json;

import br.ufrn.EchoTyper.calendar.service.CalendarService;

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
