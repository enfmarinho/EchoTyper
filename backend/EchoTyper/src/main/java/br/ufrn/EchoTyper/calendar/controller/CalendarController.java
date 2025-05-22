package br.ufrn.EchoTyper.calendar.controller;

import java.time.LocalDate;
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
    public ResponseEntity<List<CalendarResponseDTO>> getAllEvents();

    @GetMapping("/{id}")
    public ResponseEntity<CalendarResponseDTO> getEventById(@PathVariable("id") Long id);

    @GetMapping("/title/{title}")
    public ResponseEntity<CalendarResponseDTO> getEventByTitle(@PathVariable("title") String title);
            
    @GetMapping("/date/{date}")
    public ResponseEntity<CalendarResponseDTO> getEventByDate(@PathVariable("date") LocalDate date);

    @PostMapping("/create")
    public ResponseEntity<CalendarResponseDTO> createEvent(@Valid @RequestBody CalendarRequestDTO createEventDTO);

    @PutMapping("/update/{id}")
    public ResponseEntity<CalendarResponseDTO> updateEvent(@PathVariable("id") Long id, @Valid @RequestBody CalendarRequestDTO updateEventDTO);

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CalendarResponseDTO> deleteEvent(@PathVariable("id") Long id);
}
