package com.example.reactiveflashcards.api.exceptionHandler.impl;

import com.example.reactiveflashcards.api.exceptionHandler.AbstractHandleException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.example.reactiveflashcards.exception.BaseErrorMessage.GENERIC_METHOD_NOT_ALLOW;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

@Slf4j
@Component
public class MethodNotAllowHandler extends AbstractHandleException<MethodNotAllowedException> {

    public MethodNotAllowHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, MethodNotAllowedException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOW.params(exchange.getRequest().getMethod().name()).getMessage();
                }).map(message -> builderError(METHOD_NOT_ALLOWED, message))
                .doFirst(() -> log.warn("=== MethodNotAllowedException: Method [{}] is not allowed at [{}] ===",
                        exchange.getRequest().getMethod(), exchange.getRequest().getPath().value(), ex))
                .flatMap(errorResponse -> writeResponse(exchange, errorResponse));
    }

    @Override
    protected boolean accept(final Throwable ex) {
        return ex instanceof MethodNotAllowedException;
    }

}
