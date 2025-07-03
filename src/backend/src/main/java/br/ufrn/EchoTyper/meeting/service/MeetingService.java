package br.ufrn.EchoTyper.meeting.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufrn.EchoTyper.register.dto.RegisterMapper;
import br.ufrn.EchoTyper.register.model.Register;
import br.ufrn.EchoTyper.register.repository.RegisterRepository;
import br.ufrn.EchoTyper.register.service.RegisterService;
import br.ufrn.EchoTyper.registerGroup.dto.RegisterGroupMapper;
import br.ufrn.EchoTyper.registerGroup.model.RegisterGroup;
import br.ufrn.EchoTyper.registerGroup.repository.RegisterGroupRepository;
import br.ufrn.EchoTyper.utils.JsonDeserializer;

@Service
public class MeetingService extends RegisterService {
    @Autowired
    public MeetingService(RegisterRepository registerRepository,
            RegisterGroupRepository registerGroupRepository,
            RegisterMapper registerMapper,
            RegisterGroupMapper registerGroupMapper) {
        super(registerRepository, registerGroupRepository, registerMapper, registerGroupMapper);
    }

    @Override
    protected void addRegisterToGroupHook(RegisterGroup group, Register register) {
        JsonNode groupParticpantsJson = group.getContent().get("participants");
        Set<String> groupParticipants = JsonDeserializer.deserialize(groupParticpantsJson.asText(), new TypeReference<Set<String>>() {
        });
        JsonNode meetingParticipantsJson = register.getContent().get("participants");
        Set<String> meetingParticipants = JsonDeserializer.deserialize(meetingParticipantsJson.asText(),
                new TypeReference<Set<String>>() {
                });
        for (String meetingParticipant : meetingParticipants) {
            groupParticipants.add(meetingParticipant);
        }
    }

    @Override
    protected void removeRegisterFromGroupHook(RegisterGroup group, Register register) {

    }

    @Override
    protected void deleteRegisterGroupHook(RegisterGroup group, Register register) {

    }
}
