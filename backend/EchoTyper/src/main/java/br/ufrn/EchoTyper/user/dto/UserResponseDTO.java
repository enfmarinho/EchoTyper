package br.ufrn.EchoTyper.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponseDTO(
        @JsonProperty("username") String username,
        @JsonProperty("email") String email) {
}
