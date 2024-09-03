package com.zholtikov.filminator.filmservice.dao;

import com.zholtikov.filminator.filmservice.model.User;

import java.util.List;

public interface UserDao {

    User create(User user);

    User update(User user);

    List<User> findAll();

    User findUserById(Long id);

    void addFriend(Long userId, Long friendId);

    List<User> findFriends(Long userId);

    List<User> findCommonFriends(Long userId, Long otherUserId);

    User removeFriend(Long userId, Long friendId);

    User deleteUser(Long userId);

    void checkUserExistence(Long id);
}
