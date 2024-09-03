package com.zholtikov.filminator.filmservice.exceptions;

public class MpaRatingNotFoundException extends RuntimeException {
    public MpaRatingNotFoundException(String message) {
        super(message);
    }
}
