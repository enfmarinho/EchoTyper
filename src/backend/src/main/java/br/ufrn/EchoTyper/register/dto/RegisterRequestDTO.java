package br.ufrn.EchoTyper.register.dto;

import java.util.Map;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.register.service.RegisterConstraints;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterRequestDTO(
        @NotNull @JsonProperty("title") @Length(min = 6, max = 50) @Pattern(regexp = RegisterConstraints.TITLE_PATTERN) String title,
        @JsonProperty("transcription") @NotNull String transcription,
        @JsonProperty("summary") @NotNull String summary,
        @JsonProperty("annotations") @NotNull String annotations,
        @JsonProperty("groupId") Long groupId,
        @NotNull @JsonProperty("content") JsonNode content) {
}
