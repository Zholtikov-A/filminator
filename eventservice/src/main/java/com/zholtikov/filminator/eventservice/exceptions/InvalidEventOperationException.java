package com.zholtikov.filminator.eventservice.exceptions;

public class InvalidEventOperationException extends RuntimeException {
    public InvalidEventOperationException(String message) {
        super(message);
    }
}
