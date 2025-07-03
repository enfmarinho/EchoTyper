package br.ufrn.EchoTyper.registerGroup.dto;

import java.util.Set;
import java.util.Map;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public record RegisterGroupRequestDTO(
    @NotNull
    @JsonProperty("groupName")
    @Length(min = 6, max = 50)
    String groupName,
    @NotNull
    @JsonProperty("registerIds")
    Set<Long> registerIds, 
    @JsonProperty("content")
    Map<String, String> content
) {
    
}
