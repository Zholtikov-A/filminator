package com.zholtikov.filminator.filmservice.model;

import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    Long eventId;
    long timestamp;
    Long userId;
    Long entityId;
    Operation operation;
    EventType eventType;

    public enum EventType {
        SCORE, FRIEND, REVIEW, LIKE
    }

    public enum Operation {
        ADD, UPDATE, REMOVE
    }
}
