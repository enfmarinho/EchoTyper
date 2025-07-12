package br.ufrn.EchoTyper.interview.dto;

import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.interview.model.HiringProcess;
import br.ufrn.EchoTyper.interview.model.Interview;
import br.ufrn.EchoTyper.register.dto.factories.RegisterGroupFactory;

public class HiringProcessFactory implements RegisterGroupFactory<HiringProcess, Interview> {

    @Override
    public HiringProcess createGroup(Long id, String groupName, Set<Interview> registers, JsonNode content) {
        return new HiringProcess(id, groupName, registers, content);
    }
    
}
