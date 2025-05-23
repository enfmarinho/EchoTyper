package br.ufrn.EchoTyper.calendar.dto;

import br.ufrn.EchoTyper.calendar.model.Calendar;

public class CalendarMapper {
    public static Calendar toEntity(CalendarRequestDTO calendarRequestDTO) {
        return new Calendar(null, calendarRequestDTO.title(), calendarRequestDTO.description(),
                calendarRequestDTO.date(), calendarRequestDTO.startTime(), calendarRequestDTO.endTime());
    }

    public static CalendarResponseDTO toResponseDTO(Calendar calendar) {
        return new CalendarResponseDTO(calendar.getId(), calendar.getTitle(), calendar.getDescription(),
                calendar.getDate(), calendar.getStartTime(), calendar.getEndTime());
    }
}
