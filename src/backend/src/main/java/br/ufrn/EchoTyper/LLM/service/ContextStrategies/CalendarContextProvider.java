package br.ufrn.EchoTyper.LLM.service.ContextStrategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.calendar.service.CalendarService;

@Service
public class CalendarContextProvider implements ContextProvider {
    
    @Autowired
    private CalendarService calendarService;

    @Override
    public String getContext(JsonNode payload) {
        return calendarService.getAllEvents().toString();
    }
}
