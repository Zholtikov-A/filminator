package com.zholtikov.filminator.userservice.storage.user;

import com.zholtikov.filminator.userservice.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    User update(User user);

    List<User> findAll();

    User findUserById(Long id);
}
