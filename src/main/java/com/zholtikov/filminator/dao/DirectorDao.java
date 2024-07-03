package com.zholtikov.filminator.dao;
import com.zholtikov.filminator.exceptions.CustomValidationException;
import com.zholtikov.filminator.model.Director;

import java.util.List;

public interface DirectorDao {

    List<Director> findAll();

    Director findById(Long id);

    Director addDirector(Director director) throws CustomValidationException;

    Director updateDirector(Director director) throws CustomValidationException;

    void deleteDirector(Long id);

}
