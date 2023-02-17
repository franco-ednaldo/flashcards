package com.example.reactiveflashcards.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ReactiveFlashcardsException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
