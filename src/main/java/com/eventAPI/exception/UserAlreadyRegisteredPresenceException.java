package com.eventAPI.exception;

public class UserAlreadyRegisteredPresenceException extends RuntimeException {

    public UserAlreadyRegisteredPresenceException(String message) {
        super(message);
    }
}
