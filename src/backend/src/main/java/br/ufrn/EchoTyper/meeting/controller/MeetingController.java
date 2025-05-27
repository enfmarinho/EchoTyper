package br.ufrn.EchoTyper.meeting.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.ResponseEntity;

import br.ufrn.EchoTyper.meeting.dto.MeetingRequestDTO;
import br.ufrn.EchoTyper.meeting.dto.MeetingResponseDTO;
import br.ufrn.EchoTyper.meetingGroup.dto.MeetingGroupRequestDTO;
import br.ufrn.EchoTyper.meetingGroup.dto.MeetingGroupResponseDTO;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/meetings")
public interface MeetingController {
        @GetMapping("")
        public ResponseEntity<List<MeetingResponseDTO>> getAllMeetings();

        @GetMapping("/groupless")
        public ResponseEntity<List<MeetingResponseDTO>> getGrouplessMeetings();

        @GetMapping("/{id}")
        public ResponseEntity<MeetingResponseDTO> getMeetingById(@PathVariable("id") Long id);

        @PostMapping("/create")
        public ResponseEntity<MeetingResponseDTO> createMeeting(@Valid @RequestBody MeetingRequestDTO createMeetingDTO);

        @PutMapping("/update/{id}")
        public ResponseEntity<MeetingResponseDTO> updateMeeting(@PathVariable("id") Long id,
                        @Valid @RequestBody MeetingRequestDTO updateMeetingDTO);

        @DeleteMapping("/delete/{id}")
        public ResponseEntity<MeetingResponseDTO> deleteMeeting(@PathVariable("id") Long id);

        @GetMapping("/groups")
        public ResponseEntity<List<MeetingGroupResponseDTO>> getAllGroups();

        @GetMapping("/groups/{id}")
        public ResponseEntity<MeetingGroupResponseDTO> getGroupById(@PathVariable("id") Long id);

        @PostMapping("/groups/create")
        public ResponseEntity<MeetingGroupResponseDTO> createGroup(
                        @Valid @RequestBody MeetingGroupRequestDTO createGroupDTO);

        @PutMapping("/groups/add/{meetingId}/{groupId}")
        public ResponseEntity<MeetingGroupResponseDTO> addMeeting(@PathVariable("meetingId") Long meetingId,
                        @PathVariable("groupId") Long groupId);

        @PutMapping("/groups/remove/{meetingId}/{groupId}")
        public ResponseEntity<MeetingGroupResponseDTO> removeMeeting(@PathVariable("meetingId") Long meetingId,
                        @PathVariable("groupId") Long groupId);

        @DeleteMapping("/groups/delete/{groupId}")
        public ResponseEntity deleteGroup(@PathVariable("groupId") Long groupId);
}
