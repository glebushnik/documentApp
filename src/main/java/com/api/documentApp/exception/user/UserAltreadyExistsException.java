package com.api.documentApp.exception.user;

public class UserAltreadyExistsException extends Exception{
    public UserAltreadyExistsException(String message) {
        super(message);
    }
}
