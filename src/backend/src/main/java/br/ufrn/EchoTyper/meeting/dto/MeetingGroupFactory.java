package br.ufrn.EchoTyper.meeting.dto;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.meeting.model.MeetingGroup;
import br.ufrn.EchoTyper.register.dto.factories.RegisterGroupFactory;
import br.ufrn.EchoTyper.register.model.Register;

@Component
public class MeetingGroupFactory implements RegisterGroupFactory<MeetingGroup> 
{

    @Override
    public MeetingGroup createGroup(Long id, String groupName, Set<Register> registers, JsonNode content) {
        return new MeetingGroup(id, groupName, registers, content);
    }
    
}
