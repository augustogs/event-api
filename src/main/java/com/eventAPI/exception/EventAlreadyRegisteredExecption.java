package com.eventAPI.exception;

public class EventAlreadyRegisteredExecption extends RuntimeException {

    public EventAlreadyRegisteredExecption(String message) {
        super(message);
    }
}
