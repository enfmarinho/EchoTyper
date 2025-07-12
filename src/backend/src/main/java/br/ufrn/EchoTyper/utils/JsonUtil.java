package br.ufrn.EchoTyper.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    public static JsonNode serialize(Object object) {
       ObjectMapper mapper = new ObjectMapper();
       return mapper.valueToTree(object);
    }

    public static <T> T deserialize(JsonNode json, TypeReference<T> typeRef) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json.traverse(), typeRef);
        } catch (Exception e) {
            System.err.println("An exception has occurred: " + e.getMessage());
            return null;
        }
    }

    public static <T> T deserialize(JsonNode json, Class<T> type) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json.traverse(), type);
        } catch (Exception e) {
            System.err.println("An exception has occurred (Class<T>): " + e.getMessage());
            return null;
        }
    }
}
