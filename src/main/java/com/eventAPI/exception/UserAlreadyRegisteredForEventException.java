package com.eventAPI.exception;

public class UserAlreadyRegisteredForEventException extends RuntimeException {

    public UserAlreadyRegisteredForEventException(String message) {
        super(message);
    }
}
