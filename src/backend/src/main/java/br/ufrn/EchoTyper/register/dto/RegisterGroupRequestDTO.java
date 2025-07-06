package br.ufrn.EchoTyper.register.dto;

import java.util.Set;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

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
    JsonNode content
) {
    
}
