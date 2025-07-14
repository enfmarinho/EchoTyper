package br.ufrn.EchoTyper.lecture.dto;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.lecture.model.Lecture;
import br.ufrn.EchoTyper.lecture.model.Syllabus;
import br.ufrn.EchoTyper.register.dto.factories.RegisterGroupFactory;

@Component
public class SyllabusFactory implements RegisterGroupFactory<Syllabus,Lecture> {

    @Override
    public Syllabus createGroup(Long id, String groupName, Set<Lecture> registers, JsonNode content) {
        return new Syllabus(id, groupName, registers, content);
    }
    
}
