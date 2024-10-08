package com.zholtikov.filminator.filmservice.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zholtikov.filminator.filmservice.model.EventMessage;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class CustomSerializer implements Serializer<EventMessage> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, EventMessage data) {
        try {
            if (data == null){
                System.out.println("Null received at serializing");
                return null;
            }
            System.out.println("Serializing...");
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing EventMessage to byte[]");
        }
    }

    @Override
    public void close() {
    }
}