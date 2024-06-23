package com.zholtikov.filminator.dao;
import com.zholtikov.filminator.exceptions.CustomValidationException;
import com.zholtikov.filminator.model.Director;

import java.util.List;

public interface DirectorStorage {

    List<Director> findAll();

    Director findById(Integer id);

    Director addDirector(Director director) throws CustomValidationException;

    Director updateDirector(Director director) throws CustomValidationException;

    void deleteDirector(int id);

}
