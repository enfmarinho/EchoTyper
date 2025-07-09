package br.ufrn.EchoTyper.meeting.model;

import br.ufrn.EchoTyper.register.model.Register;
import br.ufrn.EchoTyper.register.model.RegisterGroup;
import br.ufrn.EchoTyper.utils.JsonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "tb_meeting")
public class Meeting extends Register {

    @Column(name = "str_participants")
    Set<String> participants;

    @Column(nullable = false, name = "dt_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date = Date.from(Instant.now());

    protected void setParticipants(Set<String> participants) {
        this.participants = participants;
    }

    public Set<String> getParticipants() {
        return participants;
    }

    protected void setDate(Date date) {
        this.date = date;
    }

    protected Date getDate() {
        return this.date;
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

        if (this.content.has("date")) {
            Date date = JsonUtil.deserialize(this.content.get("date"),
                    new TypeReference<Date>() {
                    });
            this.setDate(date); // the date has a default value
        }
    }

    @Override
    public JsonNode getContent() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode contentNode = mapper.createObjectNode();
        contentNode.put("date", this.getDate().toString());
        contentNode.putPOJO("participants", this.getParticipants());
        return contentNode;
    }

    public Meeting() {
        super();
    }

    public Meeting(Long id, String title, String transcription, String summary, String annotations,
            JsonNode content) {
        super(id, title, transcription, summary, annotations,
                content);
    }

    // ! : O uso do RegisterGroup como parametro pode causar erro
    public Meeting(Long id, String title, String transcription, String summary, String annotations,
            RegisterGroup<Register> group, JsonNode content) {
        super(id, title, transcription, summary, annotations,
                group, content);
    }
}
