package br.ufrn.EchoTyper.meeting.model;

import br.ufrn.EchoTyper.register.model.Register;
import br.ufrn.EchoTyper.registerGroup.model.RegisterGroup;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;

import java.security.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "tb_meeting")
public class Meeting extends Register {


    @Column(nullable = false, name = "str_participants")
    Set<String> participants ;

    public void setParticipants(Set<String> participants) {
        this.participants = participants;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Nao sei se a deserializacao da string nos respectivos objetos deve ser feita
    // na subclasse ou na classe abstrata 
    public void computeParticipants(Set<String> participants) {
        this.participants = participants;
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
            RegisterGroup group, JsonNode content) {
        super(id,title,transcription, summary,annotations,
            group, content);
    }
}
