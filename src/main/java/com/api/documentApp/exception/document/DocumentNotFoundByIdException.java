package com.api.documentApp.exception.document;

public class DocumentNotFoundByIdException extends Exception{
    public DocumentNotFoundByIdException(String message) {
        super(message);
    }
}
