package br.ufrn.EchoTyper.register.model;

import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tb_register_group")
public abstract class RegisterGroup<RegisterImpl extends Register> {

    @Transient
    protected final String SUBCLASS_ATTR_NAME_FORMAT = "compute%s";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull(message = "Group name is required")
    @Size(min = 3, max = 50, message = "Group name must be between 3 and 50 characters")
    @Column(nullable = false, name = "str_group_name")
    protected String groupName;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    protected Set<RegisterImpl> registers;

    @Transient
    protected JsonNode content;

    public RegisterGroup() {
    }

    public RegisterGroup(Long id, String groupName, Set<RegisterImpl> registers, JsonNode content) {
        this.groupName = groupName;
        this.registers = registers;
        this.id = id;
        setContent(content);
    }

    public Long getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public Set<RegisterImpl> getRegisters() {
        return registers;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setRegisters(Set<RegisterImpl> registers) {
        this.registers = registers;
    }

    public JsonNode getContent() {
        return content;
    }

    public abstract void setContent(JsonNode json);

}
