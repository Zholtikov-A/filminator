package com.zholtikov.filminator.dao;

import com.zholtikov.filminator.model.Event;

import java.util.List;

public interface EventDao {
    List<Event> getEvents(Integer userId);

    void addScore(Long userId, Long filmId);

    void addFriend(Long userId, Long userId1);

    void addReview(Long userId, Long reviewId);

    void removeScore(Long userId, Long filmId);

    void removeFriend(Long userId, Long userId1);

    void removeReview(Long userId, Long reviewId);

    void updateReview(Long userId, Long reviewId);
}
