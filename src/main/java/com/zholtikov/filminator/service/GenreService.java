package com.zholtikov.filminator.service;

import com.zholtikov.filminator.dao.GenreDao;
import com.zholtikov.filminator.model.Genre;
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
