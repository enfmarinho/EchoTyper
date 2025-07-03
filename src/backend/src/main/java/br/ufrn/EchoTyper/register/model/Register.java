package br.ufrn.EchoTyper.register.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufrn.EchoTyper.registerGroup.model.RegisterGroup;
import br.ufrn.EchoTyper.utils.JsonDeserializer;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@MappedSuperclass
public abstract class Register {

    @Transient
    protected final String SUBCLASS_ATTR_NAME_FORMAT = "compute%s";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "Title is required")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    @Column(nullable = false, name = "str_title")
    String title;

    @Column(nullable = false, name = "str_transcription")
    @Lob
    String transcription;

    @Lob
    @Column(nullable = false, name = "str_summary")
    String summary;

    @Lob
    @Column(nullable = false, name = "str_annotations")
    String annotations;

    @ManyToOne(optional = true)
    @JoinColumn(name = "group_id", nullable = true) // Not every register has to be part of a group
    RegisterGroup group;

    @Transient
    JsonNode content;

    public Register(Long id, String title, String transcription, String summary, String annotations,
            RegisterGroup group) {
        this.id = id;
        this.title = title;
        this.transcription = transcription;
        this.summary = summary;
        this.annotations = annotations;
        this.group = group;
    }

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

    public RegisterGroup getGroup() {
        return group;
    }

    public void setGroup(RegisterGroup group) {
        this.group = group;
    }

    protected <T> T deserialize(String json, Class<T> objectClass) {
        ObjectMapper mapper = new ObjectMapper();
        T deserializedObj = null;
        try {
            deserializedObj = mapper.readValue(json, objectClass);
        } catch (Exception e) {
            System.err.println("An exception has occurred" + e.getMessage());
        }
        return deserializedObj;
    }

    public void setSubclassesAttributes(JsonNode content) {
        Class<?> subclass = this.getClass();
        for (Field attribute : subclass.getDeclaredFields()) {
            String attributeName = attribute.getName();
            if (content.hasNonNull(attributeName)) {
                try {
                    String capitalizedAttributeName = attributeName.substring(0, 0).toUpperCase()
                            + attributeName.substring(1);
                    String methodName = String.format(SUBCLASS_ATTR_NAME_FORMAT, capitalizedAttributeName);
                    Method method = subclass.getMethod(methodName);
                    Class<?> attributeClass = method.getParameterTypes()[0];
                    method.invoke(JsonDeserializer.deserialize(attributeName, attributeClass));
                } catch (Exception e) {
                    System.err.println("An exception has occurred" + e.getMessage());
                }
            } else {
                throw new IllegalArgumentException();
            }
        }
        this.content = content;
    }

    // TODO : Eliminar escape de referencia
    public JsonNode getContent() {
        return content;
    }

    public Register() {
    };

    public Register(Long id, String title, String transcription, String summary, String annotations,
            JsonNode content) {
        this.id = id;
        this.title = title;
        this.transcription = transcription;
        this.summary = summary;
        this.annotations = annotations;
        setSubclassesAttributes(content);
    };

    public Register(Long id, String title, String transcription, String summary, String annotations,
            RegisterGroup group, JsonNode content) {
        this(id, title, transcription, summary, annotations, content);
        this.group = group;
    }
}
