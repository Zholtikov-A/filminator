package com.zholtikov.filminator.filmservice.dao;

import com.zholtikov.filminator.filmservice.model.Mpa;

import java.util.List;

public interface MpaDao {

    List<Mpa> findAll();

    Mpa findMpaById(Long id);

    void checkMpaExistence(Long id);

}
