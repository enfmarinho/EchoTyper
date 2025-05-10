package br.ufrn.EchoTyper.meeting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MeetingResponseDTO(
        @JsonProperty("title") String title,
        @JsonProperty("transcription") String transcription,
        @JsonProperty("summary") String summary,
        @JsonProperty("annotations") String annotations) {
}
