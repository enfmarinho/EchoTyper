package br.ufrn.EchoTyper.register.model;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

@MappedSuperclass
public abstract class FrameworkEntity {
    @Transient
    protected final String SUBCLASS_ATTR_NAME_FORMAT = "compute%s";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Transient
    protected JsonNode content;

    public FrameworkEntity() {}

    public FrameworkEntity(Long id, JsonNode content) {
        this.id = id;
        this.content = content;
        processContent();
    }

    public Long getId() {
        return id;
    }

    public JsonNode getContent() {
        return content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(JsonNode content) {
        this.content = content;
        processContent();
    }

    public abstract void processContent();
}
