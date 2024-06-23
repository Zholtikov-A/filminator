package com.zholtikov.filminator.controller;

import com.zholtikov.filminator.model.Director;
import com.zholtikov.filminator.service.DirectorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public List<Director> getDirectors() {
        List<Director> directors = directorService.getDirectors();
        log.info("Get directors list");
        return directors;
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable int id) {
        Director director = directorService.getDirectorById(id);
        log.info("Get director with id{}", id);
        return director;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Director addDirector(@Valid @RequestBody Director director) {
        Director newDirector = directorService.addDirector(director);
        log.info("Add director with id{}", newDirector.getId());
        return newDirector;
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        Director updatedDirector = directorService.updateDirector(director);

        log.info("Update director with id{}", director.getId());
        return updatedDirector;
    }

    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable int id) {
        directorService.deleteDirector(id);
        log.info("Remove director with id{}", id);
    }

}
