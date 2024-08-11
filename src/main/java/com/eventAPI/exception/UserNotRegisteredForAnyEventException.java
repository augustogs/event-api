package com.eventAPI.exception;

public class UserNotRegisteredForAnyEventException extends RuntimeException {

    public UserNotRegisteredForAnyEventException(String message) {
        super(message);
    }
}
