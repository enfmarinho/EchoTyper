package br.ufrn.EchoTyper.meetingGroup.model;

import java.util.Set;

import br.ufrn.EchoTyper.meeting.model.Meeting;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_meeting_groups")
public class MeetingGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Group name is required")
    @Size(min = 3, max = 50, message = "Group name must be between 3 and 50 characters")
    @Column(nullable = false, name = "str_group_name")
    private String groupName;

    @OneToMany(mappedBy = "group")
    private Set<Meeting> meetings;

    public MeetingGroup() {

    }

    public MeetingGroup(Long id, String groupName, Set<Meeting> meetings) {
        this.groupName = groupName;
        this.meetings = meetings;
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public Long getId() {
        return id;
    }

    public Set<Meeting> getMeetings() {
        return meetings;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMeetings(Set<Meeting> meetings) {
        this.meetings = meetings;
    }
}
