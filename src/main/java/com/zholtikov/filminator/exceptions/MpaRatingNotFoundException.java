package com.zholtikov.filminator.exceptions;

public class MpaRatingNotFoundException extends RuntimeException {
    public MpaRatingNotFoundException(String message) {
        super(message);
    }
}
