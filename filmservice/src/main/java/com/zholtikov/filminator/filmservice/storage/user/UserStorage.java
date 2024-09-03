package com.zholtikov.filminator.filmservice.storage.user;

import com.zholtikov.filminator.filmservice.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    User update(User user);

    List<User> findAll();

    User findUserById(Long id);
}
