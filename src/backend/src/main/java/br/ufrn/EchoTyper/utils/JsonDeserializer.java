package br.ufrn.EchoTyper.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDeserializer {
    public static <T> T deserialize(String json, TypeReference<T> typeRef) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, typeRef);
        } catch (Exception e) {
            System.err.println("An exception has occurred: " + e.getMessage());
            return null;
        }
    }

    public static <T> T deserialize(String json, Class<T> type) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            System.err.println("An exception has occurred (Class<T>): " + e.getMessage());
            return null;
        }
    }
}
