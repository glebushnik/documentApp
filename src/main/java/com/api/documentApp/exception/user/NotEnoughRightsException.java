package com.api.documentApp.exception.user;

public class NotEnoughRightsException extends Exception{
    public NotEnoughRightsException(String message) {
        super(message);
    }
}
