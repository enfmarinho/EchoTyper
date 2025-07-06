package br.ufrn.EchoTyper.register.dto;

import java.util.Collection;

import org.springframework.stereotype.Component;

import br.ufrn.EchoTyper.register.dto.factories.RegisterFactory;
import br.ufrn.EchoTyper.register.model.Register;
import br.ufrn.EchoTyper.register.model.RegisterGroup;


@Component
public class RegisterMapper<RegisterImpl extends Register> {

    private final RegisterFactory<RegisterImpl> registerFactory;

    public RegisterMapper(RegisterFactory<RegisterImpl> registerFactory) {
        this.registerFactory = registerFactory;
    }

    public RegisterImpl toEntity(RegisterRequestDTO registerRequestDTO) {
        return registerFactory.createSoloRegister(null, registerRequestDTO.title(), registerRequestDTO.transcription(),
                registerRequestDTO.summary(), registerRequestDTO.annotations(),
                registerRequestDTO.content());
    }

    public RegisterImpl toEntity(RegisterRequestDTO registerRequestDTO, RegisterGroup<RegisterImpl> group) {
        return registerFactory.createGroupedRegister(null, registerRequestDTO.title(),
                registerRequestDTO.transcription(),
                registerRequestDTO.summary(), registerRequestDTO.annotations(), group,
                registerRequestDTO.content());
    }

    public RegisterResponseDTO toResponseDTO(RegisterImpl register) {
        if (register.getGroup() != null) {
            return new RegisterResponseDTO(register.getId(), register.getTitle(), register.getTranscription(),
                    register.getSummary(), register.getAnnotations(), register.getGroup().getGroupName(),
                    register.getGroup().getId(), register.getContent());
        }
        return new RegisterResponseDTO(register.getId(), register.getTitle(), register.getTranscription(),
                register.getSummary(), register.getAnnotations(), null, null, register.getContent());
    }

    public Collection<RegisterResponseDTO> toResponseDTO(Collection<RegisterImpl> registers) {
        return registers.stream().map((register) -> this.toResponseDTO(register)).toList();
    }
}