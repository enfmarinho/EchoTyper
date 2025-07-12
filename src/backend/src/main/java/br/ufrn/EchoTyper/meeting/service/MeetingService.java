package br.ufrn.EchoTyper.meeting.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;


import br.ufrn.EchoTyper.meeting.model.Meeting;
import br.ufrn.EchoTyper.meeting.model.MeetingGroup;
import br.ufrn.EchoTyper.register.dto.RegisterGroupMapper;
import br.ufrn.EchoTyper.register.dto.RegisterMapper;
import br.ufrn.EchoTyper.register.repository.RegisterGroupRepository;
import br.ufrn.EchoTyper.register.repository.RegisterRepository;
import br.ufrn.EchoTyper.register.service.RegisterService;
import jakarta.transaction.Transactional;

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
        return;

    }

    // @Override
    // protected void deleteRegisterGroupHook(MeetingGroup group, Meeting register) {
    //     return;

    // }

    @Override
    @Transactional
    public List<String> getGroupContext(Long groupId) {
        Collection<Meeting> meetings = getGroupsRegistersObjs(groupId);
        List<String> summaries = new ArrayList<>();
        for (Meeting meeting : meetings) {
            summaries.add(meeting.getSummary());
        }
        return summaries;
    }
}
