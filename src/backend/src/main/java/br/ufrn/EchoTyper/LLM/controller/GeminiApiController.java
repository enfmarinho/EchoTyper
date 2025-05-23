package br.ufrn.EchoTyper.LLM.controller;

import br.ufrn.EchoTyper.LLM.service.GeminiApiService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.logging.Logger;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/gemini")
public class GeminiApiController {

    private static final Logger LOG = Logger.getLogger(GeminiApiController.class.getName());

    @Autowired
    private GeminiApiService geminiApiService;

    @PostMapping("/check-conflicts")
    public ResponseEntity<HashMap<String, Object>> checkConflicts(@RequestBody JsonNode payload) {
        LOG.info("\nINSIDE CLASS == GeminiApiController, METHOD == checkConflicts(); ");

        try {
            JsonNode result = geminiApiService.checkConflicts(payload);

            if (result != null) {
                LOG.info("Conflict analysis completed.");
                return getResponseFormat(HttpStatus.OK, "Analysis successful", result);
            } else {
                LOG.warning("Conflict analysis returned null.");
                return getResponseFormat(HttpStatus.INTERNAL_SERVER_ERROR, "Analysis failed", null);
            }

        } catch (Exception e) {
            LOG.severe("Error in checkConflicts(): " + e.getMessage());
            return getResponseFormat(HttpStatus.INTERNAL_SERVER_ERROR, "Critical Error: " + e.getLocalizedMessage(), null);
        }
    }

    public ResponseEntity<HashMap<String, Object>> getResponseFormat(HttpStatus status, String message, Object data) {
        int responseStatus = (status.equals(HttpStatus.OK)) ? 1 : 0;

        HashMap<String, Object> map = new HashMap<>();
        map.put("responseCode", responseStatus);
        map.put("message", message);
        map.put("data", data);
        return ResponseEntity.status(status).body(map);
    }
}
