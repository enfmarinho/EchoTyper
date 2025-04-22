package br.ufrn.EchoTyper.user.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Pattern;

public record UserResponseDTO(
        @JsonProperty("username") String username,
        @JsonProperty("email") String email) {
}
