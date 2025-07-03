package br.ufrn.EchoTyper.registerGroup.dto;

import java.util.Set;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.register.dto.RegisterResponseDTO;

public record RegisterGroupResponseDTO(
                @JsonProperty("id") Long id,
                @JsonProperty("groupName") String groupName,
                @JsonProperty("registers") Set<RegisterResponseDTO> registers,
                @JsonProperty("content") JsonNode content) {
}