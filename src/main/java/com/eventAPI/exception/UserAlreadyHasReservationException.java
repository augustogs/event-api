package com.eventAPI.exception;

public class UserAlreadyHasReservationException extends RuntimeException {

    public UserAlreadyHasReservationException(String message) {
        super(message);
    }
}
