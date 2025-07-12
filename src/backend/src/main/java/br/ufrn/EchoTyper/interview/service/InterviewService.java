package br.ufrn.EchoTyper.interview.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.ufrn.EchoTyper.interview.model.HiringProcess;
import br.ufrn.EchoTyper.interview.model.Interview;
import br.ufrn.EchoTyper.register.dto.RegisterGroupMapper;
import br.ufrn.EchoTyper.register.dto.RegisterMapper;
import br.ufrn.EchoTyper.register.repository.RegisterGroupRepository;
import br.ufrn.EchoTyper.register.repository.RegisterRepository;
import br.ufrn.EchoTyper.register.service.RegisterService;

public class InterviewService extends RegisterService<Interview, HiringProcess> {

    public InterviewService(RegisterRepository<Interview> registerRepository,
            RegisterGroupRepository<HiringProcess, Interview> registerGroupRepository,
            RegisterMapper<Interview> registerMapper,
            RegisterGroupMapper<HiringProcess, Interview> registerGroupMapper) {
        super(registerRepository, registerGroupRepository, registerMapper, registerGroupMapper);
    }

    @Override
    protected void addRegisterToGroupHook(HiringProcess group, Interview register) {
        group.getInterviewers().add(register.getInterviewer());
        group.getCandidates().add(register.getCandidate());
        // Adds the evaluation
        String evaluation = register.getEvaluation();
        group.getEvaluations().get(register.getCandidate()).add(evaluation);
    }

    @Override
    protected void removeRegisterFromGroupHook(HiringProcess group, Interview register) {
        group.getEvaluations().remove(register.getCandidate());

        boolean shouldRemoveInterview = true;
        for (Interview interview : group.getRegisters()){
            if (interview.getInterviewer() == register.getInterviewer()) {
                shouldRemoveInterview =false;
                break;
            }
        }

        if (shouldRemoveInterview) {
            group.getInterviewers().remove(register.getInterviewer());
        }
    }

    // @Override
    // protected void deleteRegisterGroupHook(HiringProcess group, Interview register) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'deleteRegisterGroupHook'");
    // }

    // @Override
    // public List<String> getGroupContext(Long groupId) {

    //     Collection<Interview> interviews = getGroupsRegistersObjs(groupId);
    //     List<String> contextList = new ArrayList<>();
    //     interviews.forEach(interview -> contextList.add(interview.getContent().asText()));
    //     return contextList;

    // }
    
}
