package com.zholtikov.filminator.dao;

import com.zholtikov.filminator.model.Genre;

import java.util.List;

public interface GenreDao {

    List<Genre> findAll();

    Genre findGenreById(Long id);

    void checkGenreExistence(Long id);

}
