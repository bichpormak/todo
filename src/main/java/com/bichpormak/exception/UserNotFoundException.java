package com.bichpormak.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String name) {
        super(name);
    }

}
