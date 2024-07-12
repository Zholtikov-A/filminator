package com.zholtikov.filminator.dao;

import com.zholtikov.filminator.model.Film;

import java.util.List;

public interface FilmDao {
    Film create(Film film);

    Film update(Film film);

    List<Film> findAll();

    Film findFilmById(Long id);

    Film addLike(Long filmId, Long userId);

    Film removeLike(Long filmId, Long userId);

    List<Film> getDirectorsFilms(Long directorId, String sortBy);

    void checkFilmExistence(Long id);

    void deleteFilm(Long id);

    void deleteAllFilms();

    List<Film> getCommonFilms(Long userId, Long friendId);

    List<Film> getPopularFilms(int count);

    List<Film> getPopularByYear(int year, int count);

    List<Film> getPopularByGenreAndYear(int year, Long genreId, int count);

    List<Film> getPopularByGenre(Long genreId, int count);

    List<Film> getRecommendFilms(Long userId);
}
