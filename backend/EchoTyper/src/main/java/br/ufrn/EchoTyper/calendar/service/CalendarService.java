package br.ufrn.EchoTyper.calendar.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.EchoTyper.calendar.dto.CalendarResponseDTO;
import br.ufrn.EchoTyper.calendar.model.Calendar;
import br.ufrn.EchoTyper.calendar.repository.CalendarRepository;
import br.ufrn.EchoTyper.calendar.dto.CalendarMapper;
import br.ufrn.EchoTyper.calendar.dto.CalendarRequestDTO;
import br.ufrn.EchoTyper.calendar.dto.CalendarMapper;

@Service
public class CalendarService {
    // TODO: Fazer o Exception Handling
    private final CalendarRepository calendarRepository;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    // TODO: Fazer o exception handling
    public CalendarResponseDTO createMeeting(CalendarRequestDTO calendarRequestDTO) {
        Calendar newMeeting = CalendarMapper.toEntity(calendarRequestDTO);
        calendarRepository.save(newMeeting);
        return CalendarMapper.toResponseDTO(newMeeting);
    }

    public CalendarResponseDTO getMeetingByTitle(String title) {
        Optional<Calendar> meetingOptional = calendarRepository.findByTitle(title);
        if (!meetingOptional.isPresent()) { // TODO check 
            throw new IllegalArgumentException("No meeting with this title exists.");
        }
        return CalendarMapper.toResponseDTO(calendarRepository.findByTitle(title).get());
    }

    public CalendarResponseDTO getMeetingByDate(LocalDate date) {
        Optional<Calendar> calendarOptional = calendarRepository.findByDate(date);
        if (!calendarOptional.isPresent()) {
            throw new IllegalArgumentException("No meeting on this date.");
        }
        return CalendarMapper.toResponseDTO(calendarRepository.findByDate(date).get());
    }

    public CalendarResponseDTO updateMeeting(Long id, CalendarRequestDTO calendarRequestDTO) {
        Calendar meeting = calendarRepository.findById(id).get();
        // System.out.println(calendarRequestDTO);
        meeting.setTitle(calendarRequestDTO.title());
        meeting.setDescription(calendarRequestDTO.description());
        meeting.setDate(calendarRequestDTO.date());
        calendarRepository.save(meeting);
        return CalendarMapper.toResponseDTO(meeting);
    }

    public CalendarResponseDTO getMeetingById(Long id) {
        // TODO: deve ser tratado como excecao?
        return calendarRepository.findById(id).map(CalendarMapper::toResponseDTO).orElse(null);
    }

    // TODO: Usar paginacao
    public List<CalendarResponseDTO> getAllMeetings() {
        return calendarRepository.findAll().parallelStream().map(CalendarMapper::toResponseDTO).toList();
    }

    public void deleteMeeting(Long id) {
        calendarRepository.deleteById(id);
    }
}
