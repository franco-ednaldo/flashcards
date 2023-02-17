package com.example.reactiveflashcards.api.exceptionHandler.impl;

import com.example.reactiveflashcards.api.controller.response.ErrorFieldResponse;
import com.example.reactiveflashcards.api.controller.response.ErrorResponse;
import com.example.reactiveflashcards.api.exceptionHandler.AbstractHandleException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.reactiveflashcards.exception.BaseErrorMessage.GENERIC_BAD_REQUEST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Component
@Slf4j
public class WebExchangeBindHandler extends AbstractHandleException<WebExchangeBindException> {
    private final MessageSource messageSource;

    public WebExchangeBindHandler(final ObjectMapper objectMapper, final MessageSource messageSource) {
        super(objectMapper);
        this.messageSource = messageSource;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, WebExchangeBindException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, BAD_REQUEST);
                    return GENERIC_BAD_REQUEST.getMessage();
                }).map(message -> builderError(BAD_REQUEST, message))
                .flatMap(response -> buildParamErrorMessage(response, ex))
                .doFirst(() -> log.warn("=== WebExchangeBindException: Method [{}] - [{}] ===",
                        exchange.getRequest().getMethod(), exchange.getRequest().getPath().value(), ex))
                .flatMap(errorResponse -> writeResponse(exchange, errorResponse));
    }

    @Override
    protected boolean accept(final Throwable ex) {
        return ex instanceof WebExchangeBindException;
    }

    protected Mono<ErrorResponse> buildParamErrorMessage(final ErrorResponse response, final WebExchangeBindException ex) {
        return Flux.fromIterable(ex.getAllErrors())
                .map(objectError -> ErrorFieldResponse.builder()
                        .name(objectError instanceof FieldError fieldError ? fieldError.getField() : objectError.getObjectName())
                        .message(messageSource.getMessage(objectError, LocaleContextHolder.getLocale()))
                        .build())
                .collectList()
                .map(errorResponses -> response.toBuilder().fields(errorResponses).build());
    }

}
