package com.zholtikov.filminator.filmservice.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zholtikov.filminator.filmservice.model.Director;
import com.zholtikov.filminator.filmservice.model.Film;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class DirectorSerializer implements Serializer<Director> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Director data) {
        try {
            if (data == null){
                System.out.println("Null received at serializing");
                return null;
            }
            System.out.println("Serializing...");
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing Director to byte[]");
        }
    }

    @Override
    public void close() {
    }
}