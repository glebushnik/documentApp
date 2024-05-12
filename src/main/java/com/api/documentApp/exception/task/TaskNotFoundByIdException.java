package com.api.documentApp.exception.task;

public class TaskNotFoundByIdException extends Exception{
    public TaskNotFoundByIdException(String message) {
        super(message);
    }
}
