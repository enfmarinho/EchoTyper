package br.ufrn.EchoTyper.lecture.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.ufrn.EchoTyper.register.model.Register;
import br.ufrn.EchoTyper.register.model.RegisterGroup;
import br.ufrn.EchoTyper.utils.JsonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Component
@Entity
@Table(name = "tb_lecture")
public class Lecture extends Register {

    @Column(nullable = false, name = "str_professor")
    String professor;

    @Column(nullable = false, name = "str_course")
    String course;

    @Column(name = "nu_grade")
    Double grade;

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    @Override
    public void processContent() {
        String course = JsonUtil.deserialize(this.content.get("course"), String.class);
        setCourse(course);

        String professor = JsonUtil.deserialize(this.content.get("professor"), String.class);
        setProfessor(professor);

        Double grade = JsonUtil.deserialize(this.content.get("grade"), Double.class);
        setGrade(grade);
    }

    @Override
    public JsonNode getContent() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode contentNode = mapper.createObjectNode();

        contentNode.put("course", this.course);
        contentNode.put("professor", this.professor);
        if (this.grade != null) {
            contentNode.put("grade", this.grade);
        }

        return contentNode;
    }

    public Lecture() {
    };

    public Lecture(Long id, String title, String transcription, String summary, String annotations,
            RegisterGroup<Register> group, JsonNode content) {
        super(id, title, transcription, summary, annotations,
                group, content);
    }

    public Lecture(Long id, String title, String transcription, String summary, String annotations,
            JsonNode content) {
        super(id, title, transcription, summary, annotations,
                content);
    }

}
