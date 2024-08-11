package com.eventAPI.exception;

public class EventFullyBookException extends RuntimeException {

    public EventFullyBookException(String message) {
        super(message);
    }
}
