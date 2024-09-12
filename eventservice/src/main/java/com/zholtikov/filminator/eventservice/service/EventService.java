package com.zholtikov.filminator.eventservice.service;

import com.zholtikov.filminator.eventservice.dao.EventRepository;
import com.zholtikov.filminator.eventservice.model.Event;
import com.zholtikov.filminator.eventservice.model.EventMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventService {

    EventRepository eventRepository;

    public void createEvent(EventMessage eventMessage) {
        Event event = new Event(Instant.now().toEpochMilli(), eventMessage.getUserId(), eventMessage.getTargetId(), eventMessage.getOperation(), eventMessage.getType());
       eventRepository.save(event);
    }

}