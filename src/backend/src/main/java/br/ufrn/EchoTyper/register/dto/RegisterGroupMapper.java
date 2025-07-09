package br.ufrn.EchoTyper.register.dto;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import br.ufrn.EchoTyper.register.dto.factories.RegisterGroupFactory;
import br.ufrn.EchoTyper.register.model.Register;
import br.ufrn.EchoTyper.register.model.RegisterGroup;

import org.springframework.stereotype.Component;

@Component
public class RegisterGroupMapper<RegisterGroupImpl extends RegisterGroup<RegisterImpl>, RegisterImpl extends Register> {

    private RegisterMapper<RegisterImpl> registerMapper;

    private final RegisterGroupFactory<RegisterGroupImpl, RegisterImpl> registerGroupFactory;

    @Autowired
    public RegisterGroupMapper(RegisterGroupFactory<RegisterGroupImpl, RegisterImpl> registerGroupFactory,
            RegisterMapper<RegisterImpl> registerMapper) {
        this.registerGroupFactory = registerGroupFactory;
        this.registerMapper = registerMapper;
    }

    public RegisterGroupImpl toEntity(RegisterGroupRequestDTO registerGroupRequestDTO, Set<RegisterImpl> registers) {
        return registerGroupFactory.createGroup(null, registerGroupRequestDTO.groupName(), registers,
                registerGroupRequestDTO.content());
    }

    public RegisterGroupResponseDTO toResponseDTO(RegisterGroupImpl registerGroup) {
        Collection<RegisterResponseDTO> registerDTOs = registerMapper.toResponseDTO(registerGroup.getRegisters());
        Set<RegisterResponseDTO> registerDTOsSet = new HashSet<>(registerDTOs);
        return new RegisterGroupResponseDTO(registerGroup.getId(), registerGroup.getGroupName(), registerDTOsSet,
                registerGroup.getContent());
    }
}
