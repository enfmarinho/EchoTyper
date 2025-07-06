package br.ufrn.EchoTyper.register.model;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tb_register")
public abstract class Register {

    @Transient
    protected final String SUBCLASS_ATTR_NAME_FORMAT = "compute%s";

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = true)
    private RegisterGroup<Register> group;

    public Register() {
    }

    public Register(Long id, String title, String transcription, String summary, String annotations,
            JsonNode content) {
        this.id = id;
        this.title = title;
        this.transcription = transcription;
        this.summary = summary;
        this.annotations = annotations;
        setContent(content);
    }

    public Register(Long id, String title, String transcription, String summary, String annotations,
            RegisterGroup<Register> group, JsonNode content) {
        this(id, title, transcription, summary, annotations, content);
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTranscription() {
        return transcription;
    }

    public String getSummary() {
        return summary;
    }

    public String getAnnotations() {
        return annotations;
    }

    public RegisterGroup<Register> getGroup() {
        return group;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }

    public void setGroup(RegisterGroup  group) {
        this.group = group;
    }

    /*
     * This method will set the subclass' specific attributes
     */
    public abstract void setContent(JsonNode json);

    public abstract JsonNode getContent();
}
