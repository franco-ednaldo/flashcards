package com.example.reactiveflashcards.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ReactiveFlashcardsException extends RuntimeException {
    private final HttpStatus httpStatus;

    public ReactiveFlashcardsException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
