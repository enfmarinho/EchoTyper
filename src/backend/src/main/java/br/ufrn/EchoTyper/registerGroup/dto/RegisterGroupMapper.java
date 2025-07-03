package br.ufrn.EchoTyper.registerGroup.dto;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import br.ufrn.EchoTyper.register.dto.RegisterMapper;
import br.ufrn.EchoTyper.register.dto.RegisterResponseDTO;
import br.ufrn.EchoTyper.register.model.Register;
import br.ufrn.EchoTyper.registerGroup.model.RegisterGroup;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class RegisterGroupMapper {

    Constructor<? extends RegisterGroup> constructor;
    private RegisterMapper registerMapper;

    @Autowired
    public RegisterGroupMapper(RegisterGroup registerGroup, RegisterMapper registerMapper) {
        this.registerMapper = registerMapper;
        try {
            this.constructor = registerGroup.getClass().getConstructor(Long.class, String.class, Set.class,
                    JsonNode.class);
        } catch (Exception e) {
            System.err.println("An exception has occurred" + e.getMessage());
        }

    }

    public RegisterGroup toEntity(RegisterGroupRequestDTO registerGroupRequestDTO, Set<Register> registers) {
        RegisterGroup registerGroup = null;
        try {
            registerGroup = constructor.newInstance(null, registerGroupRequestDTO.groupName(), registers,
                    registerGroupRequestDTO.content());
        } catch (Exception e) {
            System.err.println("An exception has occurred" + e.getMessage());
        }
        return registerGroup;
    }

    public RegisterGroupResponseDTO toResponseDTO(RegisterGroup registerGroup) {
        Collection<RegisterResponseDTO> registerDTOs = registerMapper.toResponseDTO(registerGroup.getRegisters());
        Set<RegisterResponseDTO> registerDTOsSet = new HashSet<>(registerDTOs);
        return new RegisterGroupResponseDTO(registerGroup.getId(), registerGroup.getGroupName(), registerDTOsSet,
                registerGroup.getContent());
    }
}
