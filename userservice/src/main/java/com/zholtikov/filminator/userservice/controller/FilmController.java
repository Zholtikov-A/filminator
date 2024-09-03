package com.zholtikov.filminator.userservice.controller;

import com.zholtikov.filminator.userservice.model.Film;
import com.zholtikov.filminator.userservice.model.Film;
import com.zholtikov.filminator.userservice.service.FilmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
@Tag(name = "Films", description = "Requests for films")
public class FilmController {

    FilmService filmService;

    @PostMapping
    @Operation(summary = "Add new film to database")
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    @Operation(summary = "Update film in the database")
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping
    @Operation(summary = "Get list of all films")
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get film by id")
    public Film findFilmById(@PathVariable("id") Long filmId) {
        return filmService.findFilmById(filmId);
    }

    @PutMapping("{id}/like/{userId}")
    @Operation(summary = "Add user like to film by id")
    public Film addLike(@PathVariable("id") @Positive Long filmId,
                        @PathVariable("userId") @Positive Long userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    @Operation(summary = "Remove user like to film by id")
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
    @Operation(summary = "Get films by director's id")
    public List<Film> getDirectorsFilms(@PathVariable Long directorId, @RequestParam String sortBy) {
        return filmService.getDirectorsFilms(directorId, sortBy);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete film by id")
    public void deleteFilm(@PathVariable Long id) {
        filmService.deleteFilm(id);
    }

    @DeleteMapping()
    @Operation(summary = "Delete all films")
    public void deleteAllFilms() {
        filmService.deleteAllFilms();
    }

    @GetMapping("/common")
    @Operation(summary = "Get common films between 2 users")
    public List<Film> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/popular")
    @Operation(summary = "Get list of popular films by genre and year")
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

