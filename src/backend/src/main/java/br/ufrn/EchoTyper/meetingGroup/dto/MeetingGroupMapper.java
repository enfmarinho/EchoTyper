package br.ufrn.EchoTyper.meetingGroup.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import br.ufrn.EchoTyper.meeting.dto.MeetingMapper;
import br.ufrn.EchoTyper.meeting.dto.MeetingResponseDTO;
import br.ufrn.EchoTyper.meeting.model.Meeting;
import br.ufrn.EchoTyper.meetingGroup.model.MeetingGroup;

public class MeetingGroupMapper {
    
    public static MeetingGroup toEntity(MeetingGroupRequestDTO meetingGroupRequestDTO, Set<Meeting> meetings) {
        return new MeetingGroup(null, meetingGroupRequestDTO.groupName(), meetings);
    }

    public static MeetingGroupResponseDTO toResponseDTO(MeetingGroup meetingGroup) {
        Collection<MeetingResponseDTO> meetingDTOs = MeetingMapper.toResponseDTO(meetingGroup.getMeetings());
        Set<MeetingResponseDTO> meetingDTOsSet = new HashSet<>(meetingDTOs);
        return new MeetingGroupResponseDTO(meetingGroup.getId(), meetingGroup.getGroupName(), meetingDTOsSet);
    }
}
