

package com.zholtikov.filminator.userservice.kafka.consumer;

import com.zholtikov.filminator.userservice.model.Director;
import com.zholtikov.filminator.userservice.model.MessageDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {


    @KafkaListener(topics = "string-topic", containerFactory = "kafkaStringListenerContainerFactory" , groupId = "my-group-id")
    public void listenString(String message) {
        System.out.println("Received message: " + message);
    }


    @KafkaListener(topics = "dto-topic", containerFactory = "kafkaListenerContainerFactory" , groupId = "my-group-id")
    public void listen(MessageDto message) {
        System.out.println("Received DTO: " + message);
        System.out.println("Object message = " + message.getMessage());
    }


    @KafkaListener(topics = "director-topic", containerFactory = "kafkaDirectorListenerContainerFactory" , groupId = "my-group-id")
    public void listenDirector(Director director) {
        System.out.println("Received director: " + director);
        System.out.println("Director name is : " + director.getName());
    }



}

