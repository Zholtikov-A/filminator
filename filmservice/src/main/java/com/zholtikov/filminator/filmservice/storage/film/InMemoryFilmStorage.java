package com.zholtikov.filminator.filmservice.storage.film;

import com.zholtikov.filminator.filmservice.exceptions.FilmNotFoundException;
import com.zholtikov.filminator.filmservice.model.Film;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InMemoryFilmStorage implements FilmStorage {

    final Map<Long, Film> films = new HashMap<>();

    Long filmIdCounter = 0L;

    Long generateFilmId() {
        return ++filmIdCounter;
    }

    @Override
    public Film create(Film film) {
        film.setId(generateFilmId());
        films.put(film.getId(), film);
        log.info("New film created: " + film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.replace(film.getId(), film);
        log.info("Film updated: " + film);
        return film;
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilmById(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException("Film with id \"" + filmId + "\" not found.");
        }
        return films.get(filmId);
    }

}

