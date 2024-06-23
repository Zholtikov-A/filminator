package com.zholtikov.filminator.storage.film;

import com.zholtikov.filminator.model.Film;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    List<Film> findAll();

    Film findFilmById(Long id);

}

