package br.ufrn.EchoTyper.meetingGroup.dto;

import java.util.Set;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public record MeetingGroupRequestDTO(
    @NotNull
    @JsonProperty("groupName")
    @Length(min = 6, max = 50)
    String groupName,
    @NotNull
    @JsonProperty("meetingIds")
    Set<Long> meetingIds 
) {
    
}
