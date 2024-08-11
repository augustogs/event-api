package com.eventAPI.exception;

public class EventAlreadyStartedException extends RuntimeException {

    public EventAlreadyStartedException(String message) {
        super(message);
    }
}
