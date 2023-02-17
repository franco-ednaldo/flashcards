package com.example.reactiveflashcards.api.exceptionHandler;

import com.example.reactiveflashcards.api.exceptionHandler.impl.*;
import com.example.reactiveflashcards.exception.ReactiveFlashcardsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;


@Component
@Order(-2)
@AllArgsConstructor
@Slf4j
public class ApiExceptionHandler implements WebExceptionHandler {

    protected final ObjectMapper objectMapper;

    private final MessageSource messageSource;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        return Mono.error(ex)
                .onErrorResume(MethodNotAllowedException.class, e -> new MethodNotAllowHandler(objectMapper).handle(exchange, e))
                .onErrorResume(ReactiveFlashcardsException.class, e -> new ReactiveFlashcardsHandler(objectMapper).handle(exchange, e))
                .onErrorResume(ConstraintViolationException.class, e -> new ConstraintViolationHandler(objectMapper).handle(exchange, e))
                .onErrorResume(WebExchangeBindException.class, e -> new WebExchangeBindHandler(objectMapper, messageSource).handle(exchange, e))
                .onErrorResume(Exception.class, e -> new GenericHandler(objectMapper).handle(exchange, e))
                .then();
    }


}
