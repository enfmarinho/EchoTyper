package br.ufrn.EchoTyper.meeting.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.EchoTyper.meeting.dto.MeetingResponseDTO;
import br.ufrn.EchoTyper.meeting.model.Meeting;
import br.ufrn.EchoTyper.meeting.repository.MeetingRepository;
import br.ufrn.EchoTyper.meetingGroup.dto.MeetingGroupMapper;
import br.ufrn.EchoTyper.meetingGroup.dto.MeetingGroupRequestDTO;
import br.ufrn.EchoTyper.meetingGroup.dto.MeetingGroupResponseDTO;
import br.ufrn.EchoTyper.meetingGroup.model.MeetingGroup;
import br.ufrn.EchoTyper.meetingGroup.repository.MeetingGroupRepository;
import jakarta.transaction.Transactional;
import br.ufrn.EchoTyper.meeting.dto.MeetingRequestDTO;
import br.ufrn.EchoTyper.meeting.dto.MeetingMapper;

@Service
public class MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private MeetingGroupRepository meetingGroupRepository;

    public MeetingService(MeetingRepository meetingRepository,
            MeetingGroupRepository meetingGroupRepository) {
        this.meetingRepository = meetingRepository;
        this.meetingGroupRepository = meetingGroupRepository;
    }

    // Fazer o exception handling
    public MeetingResponseDTO createMeeting(MeetingRequestDTO meetingRequestDTO) {
        Meeting newMeeting;
        if (meetingRequestDTO.groupId() == null) {
            newMeeting = MeetingMapper.toEntity(meetingRequestDTO);
        } else {
            // The frontend flow that i have in mind guarantees that the meeting group will
            // exist by the time the meeting is created. The user will either search for a
            // certain group, create a new group for the meeting or select no group
            MeetingGroup group = meetingGroupRepository.findById(meetingRequestDTO.groupId()).orElseGet(() -> null);
            newMeeting = MeetingMapper.toEntity(meetingRequestDTO, group);
        }
        meetingRepository.save(newMeeting);
        return MeetingMapper.toResponseDTO(newMeeting);
    }

    public MeetingResponseDTO updateMeeting(Long id, MeetingRequestDTO meetingRequestDTO) {
        Meeting meeting = meetingRepository.findById(id).get();
        if (meeting == null) {
            throw new RuntimeException("Meeting not found");
        }
        meeting.setTitle(meetingRequestDTO.title());
        meeting.setTranscription(meetingRequestDTO.transcription());
        meeting.setSummary(meetingRequestDTO.summary());
        meeting.setAnnotations(meetingRequestDTO.annotations());
        MeetingGroup group = getGroupObjById(id);
        meeting.setGroup(group);
        meetingRepository.save(meeting);
        return MeetingMapper.toResponseDTO(meeting);
    }

    // Meant to be used only on the resource's service layers
    public Meeting updateMeeting(Meeting newMeeting) {
        meetingRepository.save(newMeeting);
        return newMeeting;
    }

    public MeetingResponseDTO getMeetingById(Long id) {
        return meetingRepository.findById(id).map(MeetingMapper::toResponseDTO).orElse(null);
    }

    public List<MeetingResponseDTO> getAllMeetings() {
        return meetingRepository.findAll().parallelStream().map(MeetingMapper::toResponseDTO).toList();
    }

    // Writing a JPQL query is better than handlng these entity objects, but
    // performing queries envolving LOBs is really annoying
    public void deleteMeeting(Long id) {
        if (!meetingRepository.existsById(id)) {
            throw new RuntimeException("Meeting not found");
        }
        Meeting meeting = meetingRepository.findById(id).get();
        if (meeting.getGroup() != null) {
            removeMeetingFromGroup(id, meeting.getGroup().getId());
        }
        meetingRepository.deleteById(id);
    }

    public List<MeetingResponseDTO> getGrouplessMeetings() {
        return meetingRepository.findAll().stream().filter((meeting) -> meeting.getGroup() == null)
                .map((meeting) -> MeetingMapper.toResponseDTO(meeting)).toList();
    }

    // This annotation guarantees that the 'meetings' set will be loaded eagerly
    // rather than lazily - the default approach. This has to be done because lazy
    // fetching
    // is not accessible to the DTO layer, i.e the MeetingGroupMapper class cannot
    // access the database to retrieve the meetings as-needed. It must be present in
    // all services returning MeetingGroupResponseDTOs
    @Transactional
    public List<MeetingGroupResponseDTO> getAllGroups() {
        return meetingGroupRepository.findAll().stream().map(MeetingGroupMapper::toResponseDTO).toList();
    }

    @Transactional
    public MeetingGroupResponseDTO createGroup(MeetingGroupRequestDTO meetingDTO) {
        Set<Meeting> meetings = new HashSet<>();
        for (Long meetingId : meetingDTO.meetingIds()) {
            meetings.add(getMeetingObjById(meetingId));
        }
        MeetingGroup meetingGroup = MeetingGroupMapper.toEntity(meetingDTO, meetings);
        meetingGroupRepository.save(meetingGroup);
        for (Long meetingIds : meetingDTO.meetingIds()) {
            addMeetingToGroup(meetingIds, meetingGroup.getId());
        }
        return MeetingGroupMapper.toResponseDTO(meetingGroup);
    }

    @Transactional
    public MeetingGroupResponseDTO addMeetingToGroup(Long meetingId, Long meetingGroupId) {
        MeetingGroup meetingGroup = getGroupObjById(meetingGroupId);
        Meeting newMeeting = meetingRepository.findById(meetingId).orElseGet(() -> null);
        meetingGroup.getMeetings().add(newMeeting);
        newMeeting.setGroup(meetingGroup);
        updateMeeting(newMeeting);
        meetingGroupRepository.save(meetingGroup);
        return MeetingGroupMapper.toResponseDTO(meetingGroup);
    }

    @Transactional
    public MeetingGroupResponseDTO removeMeetingFromGroup(Long meetingId, Long meetingGroupId) {
        MeetingGroup group = meetingGroupRepository.findById(meetingGroupId).get();
        Meeting meeting = getMeetingObjById(meetingGroupId);
        group.getMeetings().remove(meeting);
        meeting.setGroup(null);
        updateMeeting(meeting);
        meetingGroupRepository.save(group);
        return MeetingGroupMapper.toResponseDTO(group);
    }

    @Transactional
    public MeetingGroupResponseDTO updateMeetingGroup(Long id, MeetingGroupRequestDTO newMeetingGroupDTO) {
        MeetingGroup meetingGroup = meetingGroupRepository.findById(id).get();
        meetingGroup.setGroupName(newMeetingGroupDTO.groupName());
        Set<Meeting> meetings = newMeetingGroupDTO.meetingIds().stream()
                .map(meetingId -> meetingRepository.findById(meetingId).orElseGet(() -> null))
                .collect(Collectors.toSet());
        meetingGroup.setMeetings(meetings);
        meetingGroupRepository.save(meetingGroup);
        return MeetingGroupMapper.toResponseDTO(meetingGroup);
    }

    public void deleteMeetingGroup(Long groupId) {
        MeetingGroup group = meetingGroupRepository.findById(groupId).get();
        for (Meeting meeting : group.getMeetings()) {
            removeMeetingFromGroup(meeting.getId(), group.getId());
        }
        meetingGroupRepository.delete(group);
    }

    public MeetingGroupResponseDTO getGroupById(Long id) {
        return meetingGroupRepository.findById(id).map(MeetingGroupMapper::toResponseDTO).orElse(null);
    }

    @Transactional
    public List<String> getSummariesByGroup(Long groupId) {
        MeetingGroup group = getGroupObjById(groupId);
        if (group == null) {
            return new ArrayList<>();
        }
        Collection<Meeting> meetings = group.getMeetings();
        List<String> summaries = new ArrayList<>();
        for (Meeting meeting : meetings) {
            summaries.add(meeting.getSummary());
        }
        return summaries;
    }

    @Transactional
    public List<MeetingResponseDTO> getMeetingsByGroup(Long groupId) {
        MeetingGroup group = getGroupObjById(groupId);
        if (group == null) {
            return new ArrayList<>();
        }
        Collection<Meeting> meetings = group.getMeetings();
        List<MeetingResponseDTO> meetingResponses = new ArrayList<>();
        for (Meeting meeting : meetings) {
            meetingResponses.add(MeetingMapper.toResponseDTO(meeting));
        }
        return meetingResponses;
    }

    protected MeetingGroup getGroupObjById(Long id) {
        return meetingGroupRepository.findById(id).orElseGet(() -> null);
    }

    protected Meeting getMeetingObjById(Long id) {
        return meetingRepository.findById(id).orElseGet(() -> null);
    }
}