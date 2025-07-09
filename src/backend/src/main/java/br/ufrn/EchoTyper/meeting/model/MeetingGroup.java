package br.ufrn.EchoTyper.meeting.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.ufrn.EchoTyper.register.model.Register;
import br.ufrn.EchoTyper.register.model.RegisterGroup;
import br.ufrn.EchoTyper.utils.JsonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Component
@Entity
@Table(name = "tb_meeting_group")
public class MeetingGroup extends RegisterGroup<Meeting> {
    @Column(name = "str_participants")
    Set<String> participants;

    public void setParticipants(Set<String> participants) {
        this.participants = participants;
    }

    public Set<String> getParticipants() {
        return participants;
    }

    @Override
    public void processContent() {
        Set<String> participants = new HashSet<>();
        if (this.content.has("participants")) {
             participants = JsonUtil.deserialize(this.content.get("participants"),
                    new TypeReference<Set<String>>() {
                    });
        }
        this.setParticipants(participants);
    }

    @Override
    public JsonNode getContent() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode contentNode = mapper.createObjectNode();
        contentNode.putPOJO("participants", this.getParticipants());
        ObjectNode finalJson = mapper.createObjectNode();
        finalJson.set("content", contentNode);
        return finalJson;
    }

    public MeetingGroup() {
    }

    public MeetingGroup(Long id, String groupName, Set<Meeting> registers, JsonNode content) {
        super(id, groupName, registers, content);
    }
}
