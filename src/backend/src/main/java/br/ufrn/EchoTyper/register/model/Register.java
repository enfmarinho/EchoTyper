package br.ufrn.EchoTyper.register.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufrn.EchoTyper.registerGroup.model.RegisterGroup;
import br.ufrn.EchoTyper.utils.JsonDeserializer;
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
    private RegisterGroup group;

    @Transient
    private JsonNode content;

    public Register() {
    }

    public Register(Long id, String title, String transcription, String summary, String annotations,
            RegisterGroup group) {
        this.id = id;
        this.title = title;
        this.transcription = transcription;
        this.summary = summary;
        this.annotations = annotations;
        this.group = group;
    }

    public Register(Long id, String title, String transcription, String summary, String annotations,
            JsonNode content) {
        this.id = id;
        this.title = title;
        this.transcription = transcription;
        this.summary = summary;
        this.annotations = annotations;
        setSubclassesAttributes(content);
    }

    public Register(Long id, String title, String transcription, String summary, String annotations,
            RegisterGroup group, JsonNode content) {
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

    public RegisterGroup getGroup() {
        return group;
    }

    public JsonNode getContent() {
        return content;
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

    public void setGroup(RegisterGroup group) {
        this.group = group;
    }

    public void setSubclassesAttributes(JsonNode content) {
        Class<?> subclass = this.getClass();
        for (Field attribute : subclass.getDeclaredFields()) {
            String attributeName = attribute.getName();
            if (content.hasNonNull(attributeName)) {
                try {
                    String capitalizedAttributeName = attributeName.substring(0, 1).toUpperCase()
                            + attributeName.substring(1);
                    String methodName = String.format(SUBCLASS_ATTR_NAME_FORMAT, capitalizedAttributeName);
                    Method method = subclass.getMethod(methodName);
                    Class<?> attributeClass = method.getParameterTypes()[0];
                    method.invoke(JsonDeserializer.deserialize(attributeName, attributeClass));
                } catch (Exception e) {
                    System.err.println("An exception has occurred: " + e.getMessage());
                }
            } else {
                throw new IllegalArgumentException("Missing attribute in content: " + attributeName);
            }
        }
        this.content = content;
    }
}
