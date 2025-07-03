package br.ufrn.EchoTyper.register.dto;

import java.util.Map;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.register.service.RegisterConstraints;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterResponseDTO(
                @JsonProperty("id") Long id,
                @JsonProperty("title") String title,
                @JsonProperty("transcription") String transcription,
                @JsonProperty("summary") String summary,
                @JsonProperty("annotations") String annotations,
                @JsonProperty("groupName") String groupName,
                @JsonProperty("groupId") Long groupId,
                @JsonProperty("content") JsonNode content) {
}
