package com.zholtikov.filminator.userservice.dao;

import com.zholtikov.filminator.userservice.model.Genre;
import com.zholtikov.filminator.userservice.model.Genre;

import java.util.List;

public interface GenreDao {

    List<Genre> findAll();

    Genre findGenreById(Long id);

    void checkGenreExistence(Long id);

}
