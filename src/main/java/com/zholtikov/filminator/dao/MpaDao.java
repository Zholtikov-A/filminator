package com.zholtikov.filminator.dao;

import com.zholtikov.filminator.model.Mpa;

import java.util.List;

public interface MpaDao {

    List<Mpa> findAll();

    Mpa findMpaById(Long id);

    void checkMpaExistence(Long id);

}
