package com.zholtikov.filminator.userservice.exceptions;

public class MpaRatingNotFoundException extends RuntimeException {
    public MpaRatingNotFoundException(String message) {
        super(message);
    }
}
