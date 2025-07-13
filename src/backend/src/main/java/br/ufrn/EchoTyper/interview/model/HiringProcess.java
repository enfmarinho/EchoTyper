package br.ufrn.EchoTyper.interview.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.register.model.RegisterGroup;
import br.ufrn.EchoTyper.utils.JsonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Component
@Entity
@Table(name = "tb_hiring_process")
public class HiringProcess extends RegisterGroup<Interview> {
    @Column(name = "str_candidates")
    Set<String> candidates;

    @Column(name = "str_interviewers")
    Set<String> interviewers;

    @Column(name = "str_evaluations")
    List<String> evaluations;

    @Column(nullable = false, name = "dt_begin_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date processBeginDate;

    @Column(nullable = true, name = "dt_end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date processEndDate;

    public Set<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(Set<String> candidates) {
        this.candidates = candidates;
    }

    public Set<String> getInterviewers() {
        return interviewers;
    }

    public void setInterviewers(Set<String> interviewers) {
        this.interviewers = interviewers;
    }

    public Date getProcessBeginDate() {
        return processBeginDate;
    }

    public void setProcessBeginDate(Date processBeginDate) {
        this.processBeginDate = processBeginDate;
    }

    public Date getProcessEndDate() {
        return processEndDate;
    }

    public void setProcessEndDate(Date processEndDate) {
        this.processEndDate = processEndDate;
    }

public List<String> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<String> evaluations) {
        this.evaluations = evaluations;
    }

    @Override
    public void processContent() {
        Set<String> candidates = JsonUtil.deserialize(this.content.get("candidates"),
                new TypeReference<Set<String>>() {
                });
        setCandidates(candidates);

        Set<String> interviewers = JsonUtil.deserialize(this.content.get("interviewers"),
                new TypeReference<Set<String>>() {
                });
        setInterviewers(interviewers);

        Date processBeginDate = JsonUtil.deserialize(this.content.get("processBeginDate"),
                new TypeReference<Date>() {
                });
        setProcessBeginDate(processBeginDate);

        Date processEndDate = JsonUtil.deserialize(this.content.get("processEndDate"),
                new TypeReference<Date>() {
                });
        setProcessEndDate(processEndDate);

        List<String> evaluations = JsonUtil.deserialize(this.content.get("evaluations"),
                new TypeReference<List<String>>() {
                });
        setEvaluations(evaluations);
    }

    public HiringProcess() {
    }

    public HiringProcess(Long id, String groupName, Set<Interview> registers, JsonNode content) {
        super(id, groupName, registers, content);
    }

}
