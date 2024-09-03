package com.zholtikov.filminator.userservice.dao;

import com.zholtikov.filminator.userservice.model.Mpa;

import java.util.List;

public interface MpaDao {

    List<Mpa> findAll();

    Mpa findMpaById(Long id);

    void checkMpaExistence(Long id);

}
