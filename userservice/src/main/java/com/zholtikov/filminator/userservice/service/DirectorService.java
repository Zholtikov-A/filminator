package com.zholtikov.filminator.userservice.service;

import com.zholtikov.filminator.userservice.dao.DirectorDao;
import com.zholtikov.filminator.userservice.model.Director;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectorService {

    private final DirectorDao directorDao;

    @Autowired
    public DirectorService(DirectorDao directorDao) {
        this.directorDao = directorDao;
    }

    public Director addDirector(Director director) {
        return directorDao.addDirector(director);
    }

    public Director updateDirector(Director director) {
        return directorDao.updateDirector(director);
    }

    public List<Director> getDirectors() {
        return directorDao.findAll();
    }

    public Director getDirectorById(Long id) {
        return directorDao.findById(id);
    }

    public void deleteDirector(Long id) {
        directorDao.deleteDirector(id);
    }

}
