package br.ufrn.EchoTyper.register.dto.factories;

import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.register.model.Register;
import br.ufrn.EchoTyper.register.model.RegisterGroup;

public interface RegisterGroupFactory<RegisterGroupImpl extends RegisterGroup, RegisterImpl extends Register > {
    RegisterGroupImpl createGroup(Long id, String groupName, Set<RegisterImpl> registers, JsonNode content);
}
