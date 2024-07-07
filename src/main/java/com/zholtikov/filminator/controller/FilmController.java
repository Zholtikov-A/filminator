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
            @PathVariable("userId") @Positive Long userId) {
        return filmService.removeLike(filmId, userId);
    }

/*    @GetMapping("/popular")
    public List<Film> findPopularFilms(
            @RequestParam(defaultValue = "10", required = false) @PositiveOrZero Integer count) {
        return filmService.findPopularFilms(count);
    }*/

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

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularByGenreAndYear(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "0") Long genreId,
            @RequestParam(defaultValue = "0") int year
    ) {
        return filmService.getPopularByGenreAndYear(year, genreId, count);
    }


  /*  @PutMapping("/{id}/score/{userId}")
    public void scoreFilm(@PathVariable("id") @Positive Long filmId,
                          @PathVariable("userId") @Positive Long userId,
                          @RequestParam
                          @Min(value = 1, message = "Minimum film score is \"1\" ")
                          @Max(value = 10, message = "Maximum film score is \"10\" ") int score) {
        filmService.scoreFilm(filmId, userId, score);
    }*/

}

