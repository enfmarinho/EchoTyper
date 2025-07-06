package br.ufrn.EchoTyper.meeting.dto;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.meeting.model.Meeting;
import br.ufrn.EchoTyper.register.dto.factories.RegisterFactory;
import br.ufrn.EchoTyper.register.model.RegisterGroup;

@Component
public class MeetingFactory implements RegisterFactory<Meeting> {

    @Override
    public Meeting createSoloRegister(Long id, String title, String transcription, String summary, String annotations,
            JsonNode content) {
        return new Meeting(id, title, transcription, summary, annotations, content);
    }

    @Override
    public Meeting createGroupedRegister(Long id, String title, String transcription, String summary,
            String annotations, RegisterGroup group, JsonNode content) {
        return new Meeting(id, title, transcription, summary, annotations, group, content);
    }

}
