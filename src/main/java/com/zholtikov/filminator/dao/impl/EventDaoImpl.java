package com.zholtikov.filminator.dao.impl;

import com.zholtikov.filminator.dao.EventDao;
import com.zholtikov.filminator.model.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import static com.zholtikov.filminator.model.Event.EventType.*;
import static com.zholtikov.filminator.model.Event.Operation.*;


@Repository
public class EventDaoImpl implements EventDao {

    private static final String QUERY_FOR_EVENT = "insert into " +
            "EVENTS(TIMESTAMP, USER_ID, ENTITY_ID, OPERATION, EVENT_TYPE) VALUES (?, ?, ?, ?, ?)";
    private final JdbcTemplate jdbcTemplate;

    public EventDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Event> getEvents(Integer userId) {
        String sqlQuery = "select* from EVENTS where USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToEvent, userId);
    }

    private Event mapRowToEvent(ResultSet resultSet, int rowNum) throws SQLException {
        return new Event(resultSet.getLong("EVENT_ID"),
                resultSet.getLong("TIMESTAMP"),
                resultSet.getLong("USER_ID"),
                resultSet.getLong("ENTITY_ID"),
                Event.Operation.valueOf(resultSet.getString("OPERATION")),
                Event.EventType.valueOf(resultSet.getString("EVENT_TYPE")));
    }

    private void insertIntoDB(Long userId, Long entityId, Event.Operation operation, Event.EventType eventType) {
        long timestamp = Instant.now().toEpochMilli();
        jdbcTemplate.update(QUERY_FOR_EVENT, timestamp, userId, entityId, operation.toString(), eventType.toString());
    }

    @Override
    public void addScore(Long userId, Long filmId) {
        insertIntoDB(userId, filmId, ADD, SCORE);
    }

    @Override
    public void addFriend(Long userId, Long userId1) {
        insertIntoDB(userId, userId1, ADD, FRIEND);
    }

    @Override
    public void addReview(Long userId, Long reviewId) {
        insertIntoDB(userId, reviewId, ADD, REVIEW);
    }

    @Override
    public void removeScore(Long userId, Long filmId) {
        insertIntoDB(userId, filmId, REMOVE, SCORE);
    }

    @Override
    public void removeFriend(Long userId, Long userId1) {
        insertIntoDB(userId, userId1, REMOVE, FRIEND);
    }

    @Override
    public void removeReview(Long userId, Long reviewId) {
        insertIntoDB(userId, reviewId, REMOVE, REVIEW);
    }

    @Override
    public void updateReview(Long userId, Long reviewId) {
        insertIntoDB(userId, reviewId, UPDATE, REVIEW);
    }
}
