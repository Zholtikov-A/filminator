package com.zholtikov.filminator.filmservice.controller;

import com.zholtikov.filminator.filmservice.model.Genre;
import com.zholtikov.filminator.filmservice.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Genres", description = "Requests for genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    @Operation(summary = "Get list of all genres")
    public List<Genre> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get genre by id")
    public Genre findGenreById(@PathVariable("id") @Positive Long id) {
        return genreService.findGenreById(id);
    }

}
