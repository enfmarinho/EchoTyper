package br.ufrn.EchoTyper.meeting.dto;

import java.util.Collection;
import java.util.List;

import br.ufrn.EchoTyper.meeting.model.Meeting;
import br.ufrn.EchoTyper.meetingGroup.model.MeetingGroup;

public class MeetingMapper {
    public static Meeting toEntity(MeetingRequestDTO meetingRequestDTO) {
        return new Meeting(null, meetingRequestDTO.title(), meetingRequestDTO.transcription(),
                meetingRequestDTO.summary(), meetingRequestDTO.annotations());
    }

    public static Meeting toEntity(MeetingRequestDTO meetingRequestDTO, MeetingGroup group) {
        return new Meeting(null, meetingRequestDTO.title(), meetingRequestDTO.transcription(),
                meetingRequestDTO.summary(), meetingRequestDTO.annotations(), group);
    }

    public static MeetingResponseDTO toResponseDTO(Meeting meeting) {
        if (meeting.getGroup() != null) {
            return new MeetingResponseDTO(meeting.getId(), meeting.getTitle(), meeting.getTranscription(),
                    meeting.getSummary(), meeting.getAnnotations(), meeting.getGroup().getGroupName(), meeting.getGroup().getId());
        }
        return new MeetingResponseDTO(meeting.getId(), meeting.getTitle(), meeting.getTranscription(),
                meeting.getSummary(), meeting.getAnnotations(), null, null);
    }

    public static Collection<MeetingResponseDTO> toResponseDTO(Collection<Meeting> meetings) {
        return meetings.stream().map((meeting) -> MeetingMapper.toResponseDTO(meeting)).toList();
    }
}
