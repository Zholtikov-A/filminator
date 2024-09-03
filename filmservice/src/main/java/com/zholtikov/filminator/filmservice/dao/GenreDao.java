package com.zholtikov.filminator.filmservice.dao;

import com.zholtikov.filminator.filmservice.model.Genre;

import java.util.List;

public interface GenreDao {

    List<Genre> findAll();

    Genre findGenreById(Long id);

    void checkGenreExistence(Long id);

}
