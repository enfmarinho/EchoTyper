package br.ufrn.EchoTyper.calendar.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate date,
        @JsonProperty("startTime")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime startTime,
        @JsonProperty("endTime")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime endTime
        )
{

}
