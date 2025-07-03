package br.ufrn.EchoTyper.register.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.Class;
import java.lang.reflect.Constructor;

import br.ufrn.EchoTyper.register.model.Register;
import br.ufrn.EchoTyper.registerGroup.model.RegisterGroup;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class RegisterMapper {
    private Constructor<? extends Register> fullConstructor;
    private Constructor<? extends Register> grouplessConstructor;

    @Autowired
    public RegisterMapper(Register register) {
        // ! Remover o exception swallowing
        try {
            Class<? extends Register> registerClass = register.getClass();
            this.grouplessConstructor = registerClass.getConstructor(Long.class, String.class, String.class,
                    String.class, String.class, Map.class);
            this.fullConstructor = registerClass.getConstructor(Long.class, String.class, String.class,
                    String.class, String.class, RegisterGroup.class, Map.class);
        } catch (Exception e) {
            System.err.println("An exception has occurred" + e.getMessage());
        }
    }

    public Register toEntity(RegisterRequestDTO registerRequestDTO) {
        Register register = null;
        try {
            register = grouplessConstructor.newInstance(null, registerRequestDTO.title(),
                    registerRequestDTO.transcription(),
                    registerRequestDTO.summary(), registerRequestDTO.annotations(),
                    registerRequestDTO.content());
        } catch (Exception e) {
            System.err.println("An exception has occurred" + e.getMessage());
        }
        return register;
    }

    public Register toEntity(RegisterRequestDTO registerRequestDTO, RegisterGroup group) {
        Register register = null;
        try {
            register = fullConstructor.newInstance(null, registerRequestDTO.title(), registerRequestDTO.transcription(),
                    registerRequestDTO.summary(), registerRequestDTO.annotations(), group,
                    registerRequestDTO.content());
        } catch (Exception e) {
            System.err.println("An exception has occurred" + e.getMessage());
        }
        return register;
    }

    public RegisterResponseDTO toResponseDTO(Register register) {
        if (register.getGroup() != null) {
            return new RegisterResponseDTO(register.getId(), register.getTitle(), register.getTranscription(),
                    register.getSummary(), register.getAnnotations(), register.getGroup().getGroupName(),
                    register.getGroup().getId(), register.getContent());
        }
        return new RegisterResponseDTO(register.getId(), register.getTitle(), register.getTranscription(),
                register.getSummary(), register.getAnnotations(), null, null, register.getContent());
    }

    public Collection<RegisterResponseDTO> toResponseDTO(Collection<Register> registers) {
        return registers.stream().map((register) -> this.toResponseDTO(register)).toList();
    }
}
