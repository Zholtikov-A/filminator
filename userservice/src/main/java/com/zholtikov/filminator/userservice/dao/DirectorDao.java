package com.zholtikov.filminator.userservice.dao;
import com.zholtikov.filminator.userservice.exceptions.CustomValidationException;
import com.zholtikov.filminator.userservice.model.Director;

import java.util.List;

public interface DirectorDao {

    List<Director> findAll();

    Director findById(Long id);

    Director addDirector(Director director) throws CustomValidationException;

    Director updateDirector(Director director) throws CustomValidationException;

    void checkDirectorExistence(Long id);

    void deleteDirector(Long id);

}
