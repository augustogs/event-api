package com.eventAPI.exception;

public class UserCheckedInException extends RuntimeException {

    public UserCheckedInException(String message) {
        super(message);
    }
}
