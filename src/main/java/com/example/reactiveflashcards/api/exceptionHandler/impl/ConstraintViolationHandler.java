package com.example.reactiveflashcards.api.exceptionHandler.impl;

import com.example.reactiveflashcards.api.exceptionHandler.AbstractHandleException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;

import static com.example.reactiveflashcards.exception.BaseErrorMessage.GENERIC_BAD_REQUEST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
@Slf4j
public class ConstraintViolationHandler extends AbstractHandleException<ConstraintViolationException> {

    public ConstraintViolationHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, ConstraintViolationException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, BAD_REQUEST);
                    return GENERIC_BAD_REQUEST.getMessage();
                }).map(message -> builderError(BAD_REQUEST, message))
                .flatMap(response -> buildParamErrorMessage(response, ex))
                .doFirst(() -> log.warn("=== ConstraintViolationException: Method [{}] [{}] ===",
                        exchange.getRequest().getMethod(), exchange.getRequest().getPath().value(), ex))
                .flatMap(errorResponse -> writeResponse(exchange, errorResponse));
    }

    @Override
    protected boolean accept(final Throwable ex) {
        return ex instanceof ConstraintViolationException;
    }
}
