package br.ufrn.EchoTyper.meetingGroup.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import br.ufrn.EchoTyper.meeting.dto.MeetingResponseDTO;

public record MeetingGroupResponseDTO(
        @JsonProperty("id") Long id,
        @JsonProperty("groupName") String groupName,
        @JsonProperty("meetings") Set<MeetingResponseDTO> meetings
) {
    
} 