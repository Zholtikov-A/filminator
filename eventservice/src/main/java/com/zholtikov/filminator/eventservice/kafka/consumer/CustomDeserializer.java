package com.zholtikov.filminator.eventservice.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zholtikov.filminator.eventservice.model.EventMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class CustomDeserializer implements Deserializer<EventMessage> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public EventMessage deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");
            return objectMapper.readValue(new String(data, "UTF-8"), EventMessage.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to EventMessage");
        }
    }

    @Override
    public void close() {
    }
}