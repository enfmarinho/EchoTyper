package br.ufrn.EchoTyper.meeting.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.ResponseEntity;

import br.ufrn.EchoTyper.meeting.dto.MeetingRequestDTO;
import br.ufrn.EchoTyper.meeting.dto.MeetingResponseDTO;
import jakarta.validation.Valid;

@RequestMapping("/meetings")
public interface MeetingController {
    @GetMapping
    public ResponseEntity<List<MeetingResponseDTO>> getAllMeetings();

    @GetMapping("/{id}")
    public ResponseEntity<MeetingResponseDTO> getMeetingById(@PathVariable Long id);

    @PostMapping("/create")
    public ResponseEntity<MeetingResponseDTO> createMeeting(@Valid @RequestBody MeetingRequestDTO createMeetingDTO);

    @PutMapping("/update/{id}")
    public ResponseEntity<MeetingResponseDTO> updateMeeting(Long id, @Valid @RequestBody MeetingRequestDTO updateMeetingDTO);

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MeetingResponseDTO> deleteMeeting(@PathVariable("id") Long id);
}
