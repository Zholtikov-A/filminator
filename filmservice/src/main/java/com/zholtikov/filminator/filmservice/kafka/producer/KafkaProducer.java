
package com.zholtikov.filminator.filmservice.kafka.producer;

import com.zholtikov.filminator.filmservice.model.Director;
import com.zholtikov.filminator.filmservice.model.EventMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaStringTemplate;

    public void sendMessage(String topic, String message) {
        kafkaStringTemplate.send(topic, message);
    }

    @Autowired
    private KafkaTemplate<String, EventMessage> kafkaEventTemplate;

    public void sendEventMessage(String topic, EventMessage message) {
        kafkaEventTemplate.send(topic, message);
    }


    @Autowired
    private KafkaTemplate<String, Director> kafkaDirectorTemplate;

    public void sendDirector(String topic, Director director) {
        kafkaDirectorTemplate.send(topic, director);
    }


}



