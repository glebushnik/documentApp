package com.glebushnik.documentApp.exception.user;

public class UserNotFoundByIdException extends Exception{
    public UserNotFoundByIdException(String message) {
        super(message);
    }
}
