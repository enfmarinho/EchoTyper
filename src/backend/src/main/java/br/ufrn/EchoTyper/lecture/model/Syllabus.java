package br.ufrn.EchoTyper.lecture.model;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.register.model.RegisterGroup;
import br.ufrn.EchoTyper.utils.JsonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Component
@Entity
@Table(name = "tb_syllabus")
public class Syllabus extends RegisterGroup<Lecture> {
    @Column(name = "grades")
    List<Double> grades;
    @Column(name = "nu_score")
    Double score = 0.;
    @Column(name = "nu_attendance")
    Double currentAttendance = 0.;
    @Column(name = "nu_workload")
    Integer workload;
    @Transient
    Integer classesAttended = 0;

    public List<Double> getGrades() {
        return this.grades;
    }

    public Double getScore() {
        return this.score;
    }

    public Double getCurrentAttendance() {
        return this.currentAttendance;
    }

    public void setGrades(List<Double> grades) {
        this.grades = grades;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setCurrentAttendance(Double currentAttendance) {
        this.currentAttendance = currentAttendance;
    }

    public Integer getWorkload() {
        return workload;
    }

    public void setWorkload(Integer workload) {
        this.workload = workload;
    }

    public void registerAttendance() {
        this.classesAttended++;
        setCurrentAttendance((double) classesAttended / workload);
    }

    public void removeAttendance() {
        this.classesAttended--;
        setCurrentAttendance((double) classesAttended / workload);
    }

    public void addGrade(Double grade) {
        this.grades.add(grade);
        // this.score = calculateScore();
    }

    public void removeGrade(Double grade) {
        this.grades.remove(grade);
        // this.score = calculateScore();
    }

    @Override
    public void processContent() {
        List<Double> grades = JsonUtil.deserialize(this.content.get("grades"), new TypeReference<List<Double>>() {
        });
        setGrades(grades);

        Double score = JsonUtil.deserialize(this.content.get("course"), Double.class);
        setScore(score);

        Double currentAttendance = JsonUtil.deserialize(this.content.get("currentAttendance"), Double.class);
        setCurrentAttendance(currentAttendance);

        Integer workload = JsonUtil.deserialize(this.content.get("workload"), Integer.class);
        setWorkload(workload);
    }

    public Syllabus(){};

    public Syllabus(Long id, String groupName, Set<Lecture> registers, JsonNode content) {  
        super(id, groupName, registers, content);
    }

}
