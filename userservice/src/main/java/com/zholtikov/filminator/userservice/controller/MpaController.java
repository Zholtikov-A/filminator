package com.zholtikov.filminator.userservice.controller;

import com.zholtikov.filminator.userservice.model.Mpa;
import com.zholtikov.filminator.userservice.service.MpaService;
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
@RequestMapping("/mpa")
@Tag(name = "MPA", description = "Requests for MPA ratings")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    @Operation(summary = "Get list of all MPA ratings")
    public List<Mpa> findAll() {
        return mpaService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get MPA rating by id")
    public Mpa findMpaById(@PathVariable("id") @Positive Long id) {
        return mpaService.findMpaById(id);
    }

}
