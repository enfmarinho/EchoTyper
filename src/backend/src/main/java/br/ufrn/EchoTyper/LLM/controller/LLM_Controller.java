package br.ufrn.EchoTyper.LLM.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/llm")
public interface LLM_Controller {
    @PostMapping("/check-conflicts")
    public ResponseEntity<HashMap<String, Object>> checkConflicts(@RequestBody JsonNode payload);

    @PostMapping("/summarize")
    public ResponseEntity<HashMap<String, Object>> summarize(@RequestBody JsonNode payload);
}
