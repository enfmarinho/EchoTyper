package br.ufrn.EchoTyper.interview.dto;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.interview.model.Interview;
import br.ufrn.EchoTyper.register.dto.factories.RegisterFactory;
import br.ufrn.EchoTyper.register.model.RegisterGroup;

@Component
public class InterviewFactory implements RegisterFactory<Interview>{

    @Override
    public Interview createSoloRegister(Long id, String title, String transcription, String summary, String annotations,
            JsonNode content) {
                return new Interview(id, title, transcription, summary, annotations, content);
    }

    @Override
    public Interview createGroupedRegister(Long id, String title, String transcription, String summary,
            String annotations, RegisterGroup group, JsonNode content) {
                return new Interview(id, title, transcription, summary, annotations, group, content);
    }
    
}
