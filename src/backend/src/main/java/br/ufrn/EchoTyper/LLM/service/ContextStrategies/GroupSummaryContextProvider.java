package br.ufrn.EchoTyper.LLM.service.ContextStrategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.meeting.service.MeetingService;

@Service
public class GroupSummaryContextProvider implements ContextProvider {
    @Autowired
    private MeetingService meetingService;

    @Override
    public String getContext(JsonNode payload) {
        JsonNode groupIdNode = payload.path("groupId");
        if (groupIdNode.isMissingNode()) {
            return "[]"; // Return empty context if no ID is provided
        }

        long groupId = groupIdNode.asLong();
        StringBuilder builder = new StringBuilder("[");
        meetingService.getGroupContext(groupId).forEach(
            summary -> builder.append(String.format("\"%s\"%n", summary))
        );
        builder.append("]");
        return builder.toString();
    }
    
}
