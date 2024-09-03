package com.zholtikov.filminator.filmservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(name = "ValidationErrorResponse")
public class ValidationErrorResponse {

    private final List<Violation> violations;

}