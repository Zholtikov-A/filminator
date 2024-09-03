package com.zholtikov.filminator.filmservice.dao;
import com.zholtikov.filminator.filmservice.exceptions.CustomValidationException;
import com.zholtikov.filminator.filmservice.model.Director;

import java.util.List;

public interface DirectorDao {

    List<Director> findAll();

    Director findById(Long id);

    Director addDirector(Director director) throws CustomValidationException;

    Director updateDirector(Director director) throws CustomValidationException;

    void checkDirectorExistence(Long id);

    void deleteDirector(Long id);

}
