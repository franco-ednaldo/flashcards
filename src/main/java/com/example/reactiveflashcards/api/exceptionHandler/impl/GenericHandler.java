package com.example.reactiveflashcards.api.exceptionHandler.impl;

import com.example.reactiveflashcards.api.exceptionHandler.AbstractHandleException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.example.reactiveflashcards.exception.BaseErrorMessage.GENERIC_EXCEPTION;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
@Slf4j
public class GenericHandler extends AbstractHandleException<Exception> {
    public GenericHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Exception ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, INTERNAL_SERVER_ERROR);
                    return GENERIC_EXCEPTION.getMessage();
                }).map(message -> builderError(INTERNAL_SERVER_ERROR, message))
                .doFirst(() -> log.warn("=== Exception: {}", ex))
                .flatMap(errorResponse -> writeResponse(exchange, errorResponse));
    }

    @Override
    protected boolean accept(final Throwable ex) {
        return ex instanceof Exception;
    }

}
