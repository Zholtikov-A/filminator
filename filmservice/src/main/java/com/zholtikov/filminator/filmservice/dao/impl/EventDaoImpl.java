package com.zholtikov.filminator.filmservice.dao.impl;

import com.zholtikov.filminator.filmservice.dao.EventDao;
import com.zholtikov.filminator.filmservice.model.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import static com.zholtikov.filminator.filmservice.model.Event.EventType.*;
import static com.zholtikov.filminator.filmservice.model.Event.Operation.*;


@Repository
public class EventDaoImpl implements EventDao {

    private final JdbcTemplate jdbcTemplate;

    public EventDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Event> getEvents(Long userId) {
        String sqlQuery = "select* from filminator.events where USER_ID = ?";
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
      final String QUERY_FOR_EVENT = "insert into " +
            "filminator.events(TIMESTAMP, USER_ID, ENTITY_ID, OPERATION, EVENT_TYPE) VALUES (" + timestamp + ", " +
              userId + ", " + entityId + ", '" +  operation + "', '" + eventType + "' );";

        jdbcTemplate.update(QUERY_FOR_EVENT);
    }


    @Override
    public void addLike(Long userId, Long filmId) {
        insertIntoDB(userId, filmId, ADD, LIKE);
    }

    @Override
    public void removeLike(Long userId, Long filmId) {
        insertIntoDB(userId, filmId, REMOVE, LIKE);
    }

    @Override
    public void addScore(Long userId, Long filmId) {
        insertIntoDB(userId, filmId, ADD, SCORE);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        insertIntoDB(userId, friendId, ADD, FRIEND);
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
