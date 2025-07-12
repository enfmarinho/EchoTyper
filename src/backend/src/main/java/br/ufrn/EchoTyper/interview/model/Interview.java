package br.ufrn.EchoTyper.interview.model;

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
@Table(name = "tb_interview")
public class Interview extends Register {

    @Column(nullable = false, name = "str_candidate")
    String candidate;

    @Column(nullable = false, name = "str_interviewer")
    String interviewer;

    @Column(nullable = false,name = "str_role")
    String role;

    @Column(nullable = false, name = "str_evaluation")
    String evaluation;

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getInterviewer() {
        return interviewer;
    }

    public void setInterviewer(String interviewer) {
        this.interviewer = interviewer;
    }

    public String getEvaluation() {
        return formatEvaluation(evaluation);
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public void processContent() {
        String candidate = JsonUtil.deserialize(this.content.get("candidate"), String.class);
        setCandidate(candidate);

        String interviewer = JsonUtil.deserialize(this.content.get("interviewer"), String.class);
        setInterviewer(interviewer);

        String role = JsonUtil.deserialize(this.content.get("role"), String.class);
        setRole(role);

        String evaluation = JsonUtil.deserialize(this.content.get("evaluation"), String.class);
        setEvaluation(evaluation);
    }

    protected String formatEvaluation(String evaluationNotes) {
        StringBuilder evaluationStringBuilder = new StringBuilder();
        String evaluationHeader = String.format("Candidate: %s%nRole: %s%nInterviewer%s%n", this.candidate, this.role, this.interviewer);
        evaluationStringBuilder.append(evaluationHeader);
        evaluationStringBuilder.append(evaluationNotes);
        return evaluationStringBuilder.toString();
    }

    @Override
    public JsonNode getContent() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode contentNode = mapper.createObjectNode();

        contentNode.put("candidate", this.candidate);
        contentNode.put("interviewer", this.interviewer);
        contentNode.put("role", this.role);
        contentNode.put("evaluation", this.evaluation);

        return contentNode;
    }

    public Interview(Long id, String title, String transcription, String summary, String annotations,
            RegisterGroup<Register> group, JsonNode content) {
        super(id, title, transcription, summary, annotations,
                group, content);
    }

    public Interview(Long id, String title, String transcription, String summary, String annotations,
            JsonNode content) {
        super(id, title, transcription, summary, annotations,
                content);
    }

}
