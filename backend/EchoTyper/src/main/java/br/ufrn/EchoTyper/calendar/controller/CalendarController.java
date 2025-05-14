package br.ufrn.EchoTyper.calendar.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ufrn.EchoTyper.calendar.dto.CalendarRequestDTO;
import br.ufrn.EchoTyper.calendar.dto.CalendarResponseDTO;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/calendar")
public interface CalendarController {
    // TODO: Usar query strings
    @GetMapping("")
    public ResponseEntity<List<CalendarResponseDTO>> getAllMeetings();

    @GetMapping("/{id}")
    public ResponseEntity<CalendarResponseDTO> getMeetingById(@PathVariable("id") Long id);

    @GetMapping("/name/{title}")
    public ResponseEntity<CalendarResponseDTO> getMeetingByTitle(@PathVariable("title") String title);

    @GetMapping("/name/{date}")
    public ResponseEntity<CalendarResponseDTO> getMeetingByDate(@PathVariable("date") String date);

    @PostMapping("/create")
    public ResponseEntity<CalendarResponseDTO> createMeeting(@Valid @RequestBody CalendarRequestDTO createMeetingDTO);

    @PutMapping("/update/{id}")
    public ResponseEntity<CalendarResponseDTO> updateMeeting(@PathVariable("id") Long id, @Valid @RequestBody CalendarRequestDTO updateMeetingDTO);

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CalendarResponseDTO> deleteMeeting(@PathVariable("id") Long id);
}
