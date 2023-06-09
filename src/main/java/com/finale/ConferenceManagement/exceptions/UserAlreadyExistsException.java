package com.finale.ConferenceManagement.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        this("User already exists");
    }
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
