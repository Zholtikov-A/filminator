package com.zholtikov.filminator.controller;

import com.zholtikov.filminator.model.Genre;
import com.zholtikov.filminator.service.GenreService;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public List<Genre> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    public Genre findGenreById(@PathVariable("id") @Positive Long id) {
        return genreService.findGenreById(id);
    }

}
