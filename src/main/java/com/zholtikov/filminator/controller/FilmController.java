package com.zholtikov.filminator.controller;

import com.zholtikov.filminator.model.Film;
import com.zholtikov.filminator.service.FilmService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/films")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmController {

    FilmService filmService;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable("id") Long filmId) {
        return filmService.findFilmById(filmId);
    }

    @PutMapping("{id}/like/{userId}")
    public Film addLike(@PathVariable("id") @Positive Long filmId,
                        @PathVariable("userId") @Positive Long userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film removeLike(
            @PathVariable("id") @Positive Long filmId,
            @PathVariable("userId") Long userId) {
        return filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(
            @RequestParam(defaultValue = "10", required = false) @PositiveOrZero Integer count) {
        return filmService.findPopularFilms(count);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getDirectorsFilms(@PathVariable Long directorId, @RequestParam String sortBy) {
        return filmService.getDirectorsFilms(directorId, sortBy);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable Long id) {
        filmService.deleteFilm(id);
    }

    @DeleteMapping()
    public void deleteAllFilms() {
        filmService.deleteAllFilms();
    }


}

