package br.ufrn.EchoTyper.calendar.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.EchoTyper.calendar.dto.CalendarMapper;
import br.ufrn.EchoTyper.calendar.dto.CalendarRequestDTO;
import br.ufrn.EchoTyper.calendar.dto.CalendarResponseDTO;
import br.ufrn.EchoTyper.calendar.model.Calendar;
import br.ufrn.EchoTyper.calendar.repository.CalendarRepository;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public CalendarResponseDTO createEvent(CalendarRequestDTO calendarRequestDTO) {
        Calendar newEvent = CalendarMapper.toEntity(calendarRequestDTO);
        calendarRepository.save(newEvent);
        return CalendarMapper.toResponseDTO(newEvent);
    }

    public CalendarResponseDTO getEventByTitle(String title) {
        Optional<Calendar> meetingOptional = calendarRepository.findByTitle(title);
        if (!meetingOptional.isPresent()) { 
            throw new IllegalArgumentException("No meeting with this title exists.");
        }
        return CalendarMapper.toResponseDTO(calendarRepository.findByTitle(title).get());
    }

    public CalendarResponseDTO getEventByDate(LocalDate date) {
        Optional<Calendar> calendarOptional = calendarRepository.findByDate(date);
        if (!calendarOptional.isPresent()) {
            throw new IllegalArgumentException("No meeting on this date.");
        }
        return CalendarMapper.toResponseDTO(calendarRepository.findByDate(date).get());
    }

    public CalendarResponseDTO updateEvent(Long id, CalendarRequestDTO calendarRequestDTO) {
        Optional<Calendar> meetingOpt = calendarRepository.findById(id);
        if (meetingOpt.isEmpty()) {
            throw new NoSuchElementException();
        }
        Calendar meeting = meetingOpt.get();
        meeting.setTitle(calendarRequestDTO.title());
        meeting.setDescription(calendarRequestDTO.description());
        meeting.setDate(calendarRequestDTO.date());
        meeting.setStartTime(calendarRequestDTO.startTime());
        meeting.setEndTime(calendarRequestDTO.endTime());
        calendarRepository.save(meeting);
        return CalendarMapper.toResponseDTO(meeting);
    }

    public CalendarResponseDTO getEventById(Long id) {
        return calendarRepository.findById(id).map(CalendarMapper::toResponseDTO).orElse(null);
    }

    // TODO: Usar paginacao
    public List<CalendarResponseDTO> getAllEvents() {
        return calendarRepository.findAll().parallelStream().map(CalendarMapper::toResponseDTO).toList();
    }

    public void deleteEvent(Long id) {
        calendarRepository.deleteById(id);
    }
}
