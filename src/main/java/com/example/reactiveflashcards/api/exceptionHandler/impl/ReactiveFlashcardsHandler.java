package com.example.reactiveflashcards.api.exceptionHandler.impl;

import com.example.reactiveflashcards.api.exceptionHandler.AbstractHandleException;
import com.example.reactiveflashcards.exception.ReactiveFlashcardsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.example.reactiveflashcards.exception.BaseErrorMessage.GENERIC_EXCEPTION;

@Component
@Slf4j
public class ReactiveFlashcardsHandler extends AbstractHandleException<ReactiveFlashcardsException> {

    public ReactiveFlashcardsHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, ReactiveFlashcardsException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, ex.getHttpStatus());
                    return GENERIC_EXCEPTION.getMessage();
                }).map(message -> builderError(ex.getHttpStatus(), message))
                .doFirst(() -> log.warn("=== ReactiveFlashcardsException: Method [{}] [{}] ===",
                        exchange.getRequest().getMethod(), exchange.getRequest().getPath().value(), ex))
                .flatMap(errorResponse -> writeResponse(exchange, errorResponse));
    }

    @Override
    protected boolean accept(final Throwable ex) {
        return ex instanceof ReactiveFlashcardsException;
    }
}
