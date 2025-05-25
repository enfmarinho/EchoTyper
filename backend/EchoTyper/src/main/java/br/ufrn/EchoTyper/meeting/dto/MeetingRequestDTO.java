package br.ufrn.EchoTyper.meeting.dto;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.ufrn.EchoTyper.meeting.service.MeetingConstraints;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MeetingRequestDTO(
                @NotNull @JsonProperty("title") @Length(min = 6, max = 50) @Pattern(regexp = MeetingConstraints.TITLE_PATTERN) String title,
                @JsonProperty("transcription") @NotNull String transcription,
                @JsonProperty("summary") @NotNull String summary,
                @JsonProperty("annotations") @NotNull String annotations,
                @JsonProperty("groupId") Long groupId)

{
        public MeetingRequestDTO(String title, String transcription, String summary, String annotations) {
                this(title, transcription, summary, annotations, null);
        }
}