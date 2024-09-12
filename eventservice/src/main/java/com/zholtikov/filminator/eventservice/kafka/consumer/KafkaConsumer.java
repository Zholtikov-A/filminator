

package com.zholtikov.filminator.eventservice.kafka.consumer;

import com.zholtikov.filminator.eventservice.model.EventMessage;
import com.zholtikov.filminator.eventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final EventService eventService;

    @KafkaListener(topics = "string-topic", containerFactory = "kafkaStringListenerContainerFactory", groupId = "my-group-id")
    public void listenString(String message) {
        System.out.println("Received message: " + message);
    }


    @KafkaListener(topics = "event-topic", containerFactory = "kafkaEventListenerContainerFactory", groupId = "my-group-id")
    public void listen(EventMessage eventMessage) {
        System.out.println(eventMessage);
        eventService.createEvent(eventMessage);

    }



}

