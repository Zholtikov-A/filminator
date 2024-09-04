package com.zholtikov.filminator.filmservice.service;

import com.zholtikov.filminator.filmservice.dao.EventDao;
import com.zholtikov.filminator.filmservice.dao.UserDao;
import com.zholtikov.filminator.filmservice.kafka.producer.KafkaProducer;
import com.zholtikov.filminator.filmservice.model.Event;
import com.zholtikov.filminator.filmservice.model.EventMessage;
import com.zholtikov.filminator.filmservice.model.EventOperation;
import com.zholtikov.filminator.filmservice.model.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    @Autowired
    private KafkaProducer kafkaProducer;

    final UserDao userDao;
    final EventDao eventDao;



    private User checkUserName(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            String invalidUserName = user.getName();
            user.setName(user.getLogin());
            log.info("Value \"" + invalidUserName + "\" of field \"name\" is invalid, value of field \"login\" " +
                    "was set up as value of field \"name\". Current value of field \"name\" is " + user.getName());
        }
        return user;
    }

    public User create(User user) {
        return userDao.create(checkUserName(user));
    }

    public User update(User user) {
        userDao.checkUserExistence(user.getId());
        return userDao.update(checkUserName(user));
    }

    public User addFriend(Long userId, Long friendId) {
        userDao.checkUserExistence(userId);
        userDao.checkUserExistence(friendId);
        userDao.addFriend(userId, friendId);

        EventMessage eventMessage = EventMessage.builder().userId(userId).targetId(friendId)
                .operation(EventOperation.ADD_FRIEND).build();
        kafkaProducer.sendEventMessage("event-topic", eventMessage);

        log.info("Users with id \"" + userId +
                "\" and \"" + friendId +
                "\" are friends now!");
        return userDao.findUserById(friendId);
    }

    public User removeFriend(Long userId, Long friendId) {
        userDao.removeFriend(userId, friendId);

        EventMessage eventMessage = EventMessage.builder().userId(userId).targetId(friendId)
                .operation(EventOperation.REMOVE_FRIEND).build();
        kafkaProducer.sendEventMessage("event-topic", eventMessage);


        log.info("Users with id \"" + userId +
                "\" and \"" + friendId +
                "\" are not friends anymore!");
        return userDao.findUserById(friendId);
    }

    public List<User> findFriends(Long userId) {
        userDao.checkUserExistence(userId);
        return userDao.findFriends(userId);
    }

    public List<User> findCommonFriends(Long userId, Long otherUserId) {
        return userDao.findCommonFriends(userId, otherUserId);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User findUserById(Long id) {
        userDao.checkUserExistence(id);
        return userDao.findUserById(id);
    }


    public List<Event> getEvents(Long userId) {
        userDao.checkUserExistence(userId);
        return eventDao.getEvents(userId);
    }

    public User deleteUser(Long userId) {
        userDao.checkUserExistence(userId);
        return userDao.deleteUser(userId);
    }

}
