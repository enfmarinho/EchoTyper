package br.ufrn.EchoTyper.meeting.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.EchoTyper.meeting.dto.MeetingResponseDTO;
import br.ufrn.EchoTyper.meeting.model.Meeting;
import br.ufrn.EchoTyper.meeting.repository.MeetingRepository;
import br.ufrn.EchoTyper.meeting.dto.MeetingRequestDTO;
import br.ufrn.EchoTyper.meeting.dto.MeetingMapper;

@Service
public class MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    // Fazer o exception handling
    public MeetingResponseDTO createMeeting(MeetingRequestDTO meetingRequestDTO) {
        Meeting newMeeting = MeetingMapper.toEntity(meetingRequestDTO);
        meetingRepository.save(newMeeting);
        return MeetingMapper.toResponseDTO(newMeeting);
    }

    public MeetingResponseDTO updateMeeting( Long id, MeetingRequestDTO meetingRequestDTO) {
        Meeting meeting = meetingRepository.findById(id).get();
        meeting.setTitle(meetingRequestDTO.title());
        meeting.setTranscription(meetingRequestDTO.transcription());
        meeting.setSummary(meetingRequestDTO.summary());
        meeting.setAnnotations(meetingRequestDTO.annotations());
        meetingRepository.save(meeting);
        return MeetingMapper.toResponseDTO(meeting);
    }

    public MeetingResponseDTO getMeetingById(Long id) {
        return meetingRepository.findById(id).map(MeetingMapper::toResponseDTO).orElse(null);
    }

    public List<MeetingResponseDTO> getAllMeetings() {
        return meetingRepository.findAll().parallelStream().map(MeetingMapper::toResponseDTO).toList();
    }
    
    public void deleteMeeting(Long id) {
        meetingRepository.deleteById(id);
    }
}