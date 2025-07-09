package br.ufrn.EchoTyper.meeting.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.meeting.model.Meeting;
import br.ufrn.EchoTyper.meeting.model.MeetingGroup;
import br.ufrn.EchoTyper.register.dto.RegisterGroupMapper;
import br.ufrn.EchoTyper.register.dto.RegisterMapper;
import br.ufrn.EchoTyper.register.repository.RegisterGroupRepository;
import br.ufrn.EchoTyper.register.repository.RegisterRepository;
import br.ufrn.EchoTyper.register.service.RegisterService;
import br.ufrn.EchoTyper.utils.JsonUtil;

@Service
public class MeetingService extends RegisterService<Meeting, MeetingGroup> {

    public MeetingService(RegisterRepository<Meeting> registerRepository,
            RegisterGroupRepository<MeetingGroup, Meeting> registerGroupRepository,
            RegisterMapper<Meeting> registerMapper,
            RegisterGroupMapper<MeetingGroup, Meeting> registerGroupMapper) {
        super(registerRepository, registerGroupRepository, registerMapper, registerGroupMapper);
    }

    @Override
    protected void addRegisterToGroupHook(MeetingGroup group, Meeting register) {
        for (String meetingParticipant : register.getParticipants()) {
            group.getParticipants().add(meetingParticipant);
        }
    }

    @Override
    protected void removeRegisterFromGroupHook(MeetingGroup group, Meeting register) {

    }

    @Override
    protected void deleteRegisterGroupHook(MeetingGroup group, Meeting register) {


    }
}
