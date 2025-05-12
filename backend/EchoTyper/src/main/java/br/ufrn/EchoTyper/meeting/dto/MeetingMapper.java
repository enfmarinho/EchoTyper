package br.ufrn.EchoTyper.meeting.dto;

import br.ufrn.EchoTyper.meeting.model.Meeting;

public class MeetingMapper {
    public static Meeting toEntity(MeetingRequestDTO meetingRequestDTO) {
     return new Meeting(null, meetingRequestDTO.title(), meetingRequestDTO.transcription(), meetingRequestDTO.summary(), meetingRequestDTO.annotations());
    }

    public static MeetingResponseDTO toResponseDTO(Meeting meeting) {
        return new MeetingResponseDTO(meeting.getId(), meeting.getTitle(), meeting.getTranscription(), meeting.getSummary(), meeting.getAnnotations());
    }
}
