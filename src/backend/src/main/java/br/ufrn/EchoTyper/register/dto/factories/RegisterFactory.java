package br.ufrn.EchoTyper.register.dto.factories;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.register.model.Register;
import br.ufrn.EchoTyper.register.model.RegisterGroup;

public interface RegisterFactory<RegisterImpl extends Register> {
    RegisterImpl createSoloRegister(Long id, String title, String transcription, String summary, String annotations,
            JsonNode content);

    RegisterImpl createGroupedRegister(Long id, String title, String transcription, String summary, String annotations,
            RegisterGroup<RegisterImpl> group, JsonNode content);
}
