package com.api.documentApp.exception.refresh;

public class RefreshTokenNotFoundByToken extends Exception{
    public RefreshTokenNotFoundByToken(String message) {
        super(message);
    }
}
