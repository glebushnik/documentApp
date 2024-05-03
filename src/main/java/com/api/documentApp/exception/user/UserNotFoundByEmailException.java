package com.api.documentApp.exception.user;

public class UserNotFoundByEmailException extends Exception{
    public UserNotFoundByEmailException(String message) {
        super(message);
    }
}
