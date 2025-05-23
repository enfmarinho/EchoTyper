package br.ufrn.EchoTyper.calendar.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CalendarResponseDTO(
        @JsonProperty("id") Long id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("date") LocalDate date,
        @JsonProperty("startTime") LocalTime startTime,
        @JsonProperty("endTime") LocalTime endTime) {
}
