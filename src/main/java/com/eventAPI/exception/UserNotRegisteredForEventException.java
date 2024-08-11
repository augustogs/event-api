package com.eventAPI.exception;

public class UserNotRegisteredForEventException extends RuntimeException {

    public UserNotRegisteredForEventException(String message) {
        super(message);
    }
}
