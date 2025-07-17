package br.ufrn.EchoTyper.register.model;

import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tb_register_group")
public abstract class RegisterGroup<RegisterImpl extends Register> extends FrameworkEntity{

    @NotNull(message = "Group name is required")
    @Size(min = 3, max = 50, message = "Group name must be between 3 and 50 characters")
    @Column(nullable = false, name = "str_group_name")
    protected String groupName;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    protected Set<RegisterImpl> registers;


    public RegisterGroup() {}

    public RegisterGroup(Long id, String groupName, Set<RegisterImpl> registers, JsonNode content) {
        super(id, content);
        this.groupName = groupName;
        this.registers = registers;
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

}