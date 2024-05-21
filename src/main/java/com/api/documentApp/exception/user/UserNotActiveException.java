package com.api.documentApp.exception.user;

public class UserNotActiveException extends Exception{
    public UserNotActiveException(String message) {
        super(message);
    }
}
