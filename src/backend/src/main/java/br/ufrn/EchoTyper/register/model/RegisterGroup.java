package br.ufrn.EchoTyper.register.model;

import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tb_register_group")
public abstract class RegisterGroup {

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
    protected Set<Register> registers;

    @Transient
    protected JsonNode content;

    public RegisterGroup() {
    }

    public RegisterGroup(Long id, String groupName, Set<Register> registers, JsonNode content) {
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

    public Set<Register> getRegisters() {
        return registers;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setRegisters(Set<Register> registers) {
        this.registers = registers;
    }

    public JsonNode getContent() {
        return content;
    }

    public abstract void setContent(JsonNode json);

    // public void setSubclassesAttributes(JsonNode content) {
    //     Class<?> subclass = this.getClass();
    //     for (Field attribute : subclass.getDeclaredFields()) {
    //         String attributeName = attribute.getName();
    //         if (content.hasNonNull(attributeName)) {
    //             try {
    //                 String capitalizedAttributeName = attributeName.substring(0, 1).toUpperCase()
    //                         + attributeName.substring(1);
    //                 String methodName = String.format(SUBCLASS_ATTR_NAME_FORMAT, capitalizedAttributeName);
    //                 Method method = subclass.getMethod(methodName);
    //                 Class<?> attributeClass = method.getParameterTypes()[0];
    //                 method.invoke(JsonUtil.deserialize(attributeName, attributeClass));
    //             } catch (Exception e) {
    //                 System.err.println("An exception has occurred: " + e.getMessage());
    //             }
    //         } else {
    //             throw new IllegalArgumentException("Missing attribute in content: " + attributeName);
    //         }
    //     }
    //     this.content = content;
    // }
}
