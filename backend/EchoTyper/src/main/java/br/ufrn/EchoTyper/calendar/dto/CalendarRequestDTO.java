package br.ufrn.EchoTyper.calendar.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.ufrn.EchoTyper.calendar.service.CalendarConstraints;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CalendarRequestDTO(
        @NotNull
        @JsonProperty("title")
        @Length(min = 1, max = 50)
        String title,
        @JsonProperty("description")
        @Length(min = 1, max = 50) 
        @NotNull
        String description,
        @JsonProperty("date")
        @Pattern(regexp = CalendarConstraints.DATE_PATTERN) 
        @Length(min=8, max=10)
        LocalDate date
        )
{

}
