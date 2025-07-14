package br.ufrn.EchoTyper.lecture.dto;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.lecture.model.Lecture;
import br.ufrn.EchoTyper.register.dto.factories.RegisterFactory;
import br.ufrn.EchoTyper.register.model.RegisterGroup;

@Component
public class LectureFactory implements RegisterFactory<Lecture> {

    @Override
    public Lecture createSoloRegister(Long id, String title, String transcription, String summary, String annotations,
            JsonNode content) {
        return new Lecture(id, title, transcription, summary, annotations, content);
    }

    @Override
    public Lecture createGroupedRegister(Long id, String title, String transcription, String summary,
            String annotations, RegisterGroup group, JsonNode content) {
                return new Lecture(id, title, transcription, summary, annotations, group, content);
    }
    
}
