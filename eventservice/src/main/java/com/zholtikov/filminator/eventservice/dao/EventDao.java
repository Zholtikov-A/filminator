package com.zholtikov.filminator.eventservice.dao;

import com.zholtikov.filminator.eventservice.model.Event;
import com.zholtikov.filminator.eventservice.model.Event;

import java.util.List;

public interface EventDao {
    List<Event> getEvents(Long userId);

    void addLike(Long userId, Long filmId);

    void removeLike(Long userId, Long filmId);



    void addScore(Long userId, Long filmId);

    void addFriend(Long userId, Long userId1);

    void addReview(Long userId, Long reviewId);

    void removeScore(Long userId, Long filmId);

    void removeFriend(Long userId, Long userId1);

    void removeReview(Long userId, Long reviewId);

    void updateReview(Long userId, Long reviewId);
}
