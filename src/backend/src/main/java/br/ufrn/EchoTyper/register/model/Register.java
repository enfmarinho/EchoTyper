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
    protected Long id;

    @NotNull(message = "Title is required")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    @Column(nullable = false, name = "str_title")
    protected String title;

    @Column(nullable = false, name = "str_transcription")
    @Lob
    protected String transcription;

    @Lob
    @Column(nullable = false, name = "str_summary")
    protected String summary;

    @Lob
    @Column(nullable = false, name = "str_annotations")
    protected String annotations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = true)
    protected RegisterGroup<Register> group;

    @Transient
    protected JsonNode content;

    public Register() {
    }

    public Register(Long id, String title, String transcription, String summary, String annotations,
            JsonNode content) {
        this.id = id;
        this.title = title;
        this.transcription = transcription;
        this.summary = summary;
        this.annotations = annotations;
        this.content = content;
        processContent();
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

    public void setContent(JsonNode content) {
        this.content = content;
        processContent();
    }

    /*
     * This method will set the subclass' specific attributes
     */
    public abstract void processContent();

    public abstract JsonNode getContent();
}
