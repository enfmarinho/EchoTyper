package br.ufrn.EchoTyper.meeting.model;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.ufrn.EchoTyper.registerGroup.model.RegisterGroup;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Component
@Entity
@Table(name = "tb_meeting_group")
public class MeetingGroup extends RegisterGroup {
    @Column(nullable = false, name = "str_participants")
    List<String> participants;

    public void computeParticipants(List<String> participants) {
        this.participants = participants;
    }

}
