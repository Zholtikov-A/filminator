package com.zholtikov.filminator.filmservice.service;

import com.zholtikov.filminator.filmservice.dao.GenreDao;
import com.zholtikov.filminator.filmservice.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDao genreDao;

    public List<Genre> findAll() {
        return genreDao.findAll();
    }

    public Genre findGenreById(Long id) {
        genreDao.checkGenreExistence(id);
        return genreDao.findGenreById(id);
    }

}
