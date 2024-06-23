package com.zholtikov.filminator.storage.user;

import com.zholtikov.filminator.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    User update(User user);

    List<User> findAll();

    User findUserById(Long id);
}
