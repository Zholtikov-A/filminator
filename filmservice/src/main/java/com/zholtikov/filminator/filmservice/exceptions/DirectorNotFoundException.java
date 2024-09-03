package com.zholtikov.filminator.filmservice.exceptions;

public class DirectorNotFoundException extends RuntimeException {
    public DirectorNotFoundException(String message) {
        super(message);
    }
}
