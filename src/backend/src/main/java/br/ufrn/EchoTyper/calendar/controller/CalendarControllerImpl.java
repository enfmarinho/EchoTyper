package br.ufrn.EchoTyper.calendar.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.EchoTyper.calendar.dto.CalendarRequestDTO;
import br.ufrn.EchoTyper.calendar.dto.CalendarResponseDTO;
import br.ufrn.EchoTyper.calendar.service.CalendarService;

@RestController
public class CalendarControllerImpl implements CalendarController {
    @Autowired
    CalendarService calendarService;

    @Override
    public ResponseEntity<CalendarResponseDTO> createEvent(CalendarRequestDTO createEventDTO) {
        return ResponseEntity.ok().body(calendarService.createEvent(createEventDTO));
    }

    @Override
    public ResponseEntity<CalendarResponseDTO> updateEvent(Long id, CalendarRequestDTO updateEventDTO) {
        return ResponseEntity.ok().body(calendarService.updateEvent(id, updateEventDTO));
    }

    @Override
    public ResponseEntity deleteEvent(Long id) {
        calendarService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<CalendarResponseDTO>> getAllEvents() {
        return ResponseEntity.ok().body(calendarService.getAllEvents());
    }

    @Override
    public ResponseEntity<CalendarResponseDTO> getEventById(Long id) {
        return ResponseEntity.ok().body(calendarService.getEventById(id));
    }

    @Override
    public ResponseEntity<CalendarResponseDTO> getEventByTitle(String title) {
        return ResponseEntity.ok().body(calendarService.getEventByTitle(title));
    }

    @Override
    public ResponseEntity<CalendarResponseDTO> getEventByDate(LocalDate date) {
        return ResponseEntity.ok().body(calendarService.getEventByDate(date));
    }
}
