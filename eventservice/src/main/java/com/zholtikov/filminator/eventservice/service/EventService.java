package com.zholtikov.filminator.eventservice.service;

import com.zholtikov.filminator.eventservice.dao.EventDao;
import com.zholtikov.filminator.eventservice.exceptions.InvalidEventOperationException;
import com.zholtikov.filminator.eventservice.model.EventMessage;
import com.zholtikov.filminator.eventservice.model.EventOperation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventService {

    EventDao eventDao;

    public void createEvent(EventMessage eventMessage) {
        EventOperation operation = eventMessage.getOperation();
        switch (operation) {
            case ADD_LIKE -> eventDao.addLike(eventMessage.getUserId(), eventMessage.getTargetId());
            case REMOVE_LIKE -> eventDao.removeLike(eventMessage.getUserId(), eventMessage.getTargetId());
            case ADD_FRIEND -> eventDao.addFriend(eventMessage.getUserId(), eventMessage.getTargetId());
            case REMOVE_FRIEND -> eventDao.removeFriend(eventMessage.getUserId(), eventMessage.getTargetId());
            case ADD_REVIEW -> eventDao.addReview(eventMessage.getUserId(), eventMessage.getTargetId());
            case REMOVE_REVIEW -> eventDao.removeReview(eventMessage.getUserId(), eventMessage.getTargetId());
            case UPDATE_REVIEW -> eventDao.updateReview(eventMessage.getUserId(), eventMessage.getTargetId());
            default ->
                    throw new InvalidEventOperationException("Event operation type \"" + operation + "\" incorrect.");
        }
    }

}