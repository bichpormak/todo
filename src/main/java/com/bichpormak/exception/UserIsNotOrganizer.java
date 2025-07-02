package com.bichpormak.exception;

public class UserIsNotOrganizer extends RuntimeException {

    public UserIsNotOrganizer(String name) {
        super(name);
    }

}
