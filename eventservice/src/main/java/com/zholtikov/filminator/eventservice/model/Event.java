package com.zholtikov.filminator.eventservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
//@NoArgsConstructor
@Entity
@Table(name = "events", schema = "filminator")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @Column(name = "EVENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long eventId;
    @Column(name = "TIMESTAMP")
    Long timestamp;
    @Column(name = "USER_ID")
    Long userId;
    @Column(name = "ENTITY_ID")
    Long entityId;
    @Enumerated(EnumType.STRING)
    @Column(name = "OPERATION")
    EventOperation operation;
    @Enumerated(EnumType.STRING)
    @Column(name = "EVENT_ENTITY_TYPE")
    EventEntityType eventEntityType;

    public Event(Long timestamp, Long userId, Long entityId, EventOperation operation, EventEntityType eventEntityType) {
        this.timestamp = timestamp;
        this.userId = userId;
        this.entityId = entityId;
        this.operation = operation;
        this.eventEntityType = eventEntityType;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", timestamp=" + timestamp +
                ", userId=" + userId +
                ", entityId=" + entityId +
                ", operation=" + operation +
                ", eventEntityType=" + eventEntityType +
                '}';
    }
/*    public enum EventEntityType {
        SCORE, FRIEND, REVIEW, LIKE
    }

    public enum Operation {
        ADD, UPDATE, REMOVE
    }*/
}


/*
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String description;

    @Column(name = "is_available")
    Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    User owner;

    @OneToOne
    ItemRequest request;

}
*/
