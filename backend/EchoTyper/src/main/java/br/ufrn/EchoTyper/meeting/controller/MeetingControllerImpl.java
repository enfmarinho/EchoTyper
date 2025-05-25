package br.ufrn.EchoTyper.meeting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.EchoTyper.meeting.service.MeetingService;
import br.ufrn.EchoTyper.meetingGroup.dto.MeetingGroupRequestDTO;
import br.ufrn.EchoTyper.meetingGroup.dto.MeetingGroupResponseDTO;
import jakarta.validation.Valid;
import br.ufrn.EchoTyper.meeting.dto.MeetingResponseDTO;
import br.ufrn.EchoTyper.meeting.dto.MeetingRequestDTO;

@RestController
public class MeetingControllerImpl implements MeetingController {
    @Autowired
    MeetingService meetingService;

    @Override
    public ResponseEntity<List<MeetingResponseDTO>> getAllMeetings() {
        return ResponseEntity.ok().body(meetingService.getAllMeetings());
    }

    @Override
    public ResponseEntity<MeetingResponseDTO> getMeetingById(Long id) {
        return ResponseEntity.ok().body(meetingService.getMeetingById(id));
    }

    @Override
    public ResponseEntity<MeetingResponseDTO> createMeeting(MeetingRequestDTO createMeetingDTO) {
        return ResponseEntity.ok().body(meetingService.createMeeting(createMeetingDTO));
    }

    @Override
    public ResponseEntity<MeetingResponseDTO> updateMeeting(Long id, MeetingRequestDTO updateMeetingDTO) {
        return ResponseEntity.ok().body(meetingService.updateMeeting(id, updateMeetingDTO));
    }

    @Override
    public ResponseEntity<MeetingResponseDTO> deleteMeeting(Long id) {
        meetingService.deleteMeeting(id);
        return ResponseEntity.ok().build();
    }
    @Override
    public ResponseEntity<List<MeetingResponseDTO>> getGrouplessMeetings() {
        return ResponseEntity.ok().body(meetingService.getGrouplessMeetings());
    }

    @Override
    public ResponseEntity<List<MeetingGroupResponseDTO>> getAllGroups() {
        return ResponseEntity.ok().body(meetingService.getAllGroups());
    }

    @Override
    public ResponseEntity deleteGroup(Long groupId) {
        meetingService.deleteMeetingGroup(groupId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<MeetingGroupResponseDTO> addMeeting(Long meetingId, Long groupId) {
        return ResponseEntity.ok().body(meetingService.addMeetingToGroup(meetingId, groupId));
    }

    @Override
    public ResponseEntity<MeetingGroupResponseDTO> removeMeeting(Long meetingId, Long groupId) {
        return ResponseEntity.ok().body(meetingService.removeMeetingFromGroup(meetingId, groupId));
    }

    @Override
    public ResponseEntity<MeetingGroupResponseDTO> createGroup(@Valid MeetingGroupRequestDTO createGroupDTO) {
        return ResponseEntity.ok().body(meetingService.createGroup(createGroupDTO));
    }
}