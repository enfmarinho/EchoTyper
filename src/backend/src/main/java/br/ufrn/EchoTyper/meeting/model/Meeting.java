package br.ufrn.EchoTyper.meeting.model;


import br.ufrn.EchoTyper.meetingGroup.model.MeetingGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_meetings")
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title is required")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    @Column(nullable = false, name = "str_title")
    private String title;

    @Column(nullable = false, name = "str_transcription")
    @Lob
    private String transcription;

    @Lob
    @Column(nullable = false, name = "str_summary")
    private String summary;

    @Lob
    @Column(nullable = false, name = "str_annotations")
    private String annotations;

    @ManyToOne(optional = true)
    @JoinColumn(name = "group_id", nullable = true) // Not every meeting has to be part of a group
    private MeetingGroup group;

    public Meeting() {
    }

    public Meeting(Long id, String title, String transcription, String summary, String annotations) {
        this.id = id;
        this.title = title;
        this.transcription = transcription;
        this.summary = summary;
        this.annotations = annotations;
    }

    public Meeting(Long id, String title, String transcription, String summary, String annotations,
            MeetingGroup group) {
        this.id = id;
        this.title = title;
        this.transcription = transcription;
        this.summary = summary;
        this.annotations = annotations;
        this.group = group;
    }

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAnnotations() {
        return annotations;
    }

    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }

    public MeetingGroup getGroup() {
        return group;
    }

    public void setGroup(MeetingGroup group) {
        this.group = group;
    }

    // public User getUser() {
    // return user;
    // }

    // public void setUser(User user) {
    // this.user = user;
    // }
}
