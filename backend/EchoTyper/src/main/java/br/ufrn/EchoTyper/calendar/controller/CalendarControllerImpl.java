package br.ufrn.EchoTyper.calendar.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.EchoTyper.calendar.dto.CalendarResponseDTO;
import br.ufrn.EchoTyper.calendar.service.CalendarService;
import io.micrometer.core.ipc.http.HttpSender.Response;
import br.ufrn.EchoTyper.calendar.dto.CalendarRequestDTO;

@RestController
public class CalendarControllerImpl implements CalendarController {
    @Autowired
    CalendarService calendarService;

    @Override
    public ResponseEntity<CalendarResponseDTO> createMeeting(CalendarRequestDTO createMeetingDTO) {
        return ResponseEntity.ok().body(calendarService.createMeeting(createMeetingDTO));
    }

    @Override
    public ResponseEntity<CalendarResponseDTO> updateMeeting(Long id, CalendarRequestDTO updateMeetingDTO) {
        return ResponseEntity.ok().body(calendarService.updateMeeting(id, updateMeetingDTO));
    }

    @Override
    public ResponseEntity deleteMeeting(Long id) {
        calendarService.deleteMeeting(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<CalendarResponseDTO>> getAllMeetings() {
        return ResponseEntity.ok().body(calendarService.getAllMeetings());
    }

    @Override
    public ResponseEntity<CalendarResponseDTO> getMeetingById(Long id) {
        return ResponseEntity.ok().body(calendarService.getMeetingById(id));
    }

    @Override
    public ResponseEntity<CalendarResponseDTO> getMeetingByTitle(String title) {
        return ResponseEntity.ok().body(calendarService.getMeetingByTitle(title));
    }

    @Override
    public ResponseEntity<CalendarResponseDTO> getMeetingByDate(LocalDate date) {
        return ResponseEntity.ok().body(calendarService.getMeetingByDate(date));
    }
}
