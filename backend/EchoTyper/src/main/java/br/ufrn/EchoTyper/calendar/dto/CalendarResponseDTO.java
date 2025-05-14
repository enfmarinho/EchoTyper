package br.ufrn.EchoTyper.calendar.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CalendarResponseDTO(
        @JsonProperty("id") Long id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("date") LocalDate date) {
}
