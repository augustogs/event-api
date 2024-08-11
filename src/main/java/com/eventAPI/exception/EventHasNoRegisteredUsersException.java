package com.eventAPI.exception;

public class EventHasNoRegisteredUsersException extends RuntimeException {

    public EventHasNoRegisteredUsersException(String message) {
        super(message);
    }
}
