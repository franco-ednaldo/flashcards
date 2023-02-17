package com.example.reactiveflashcards.api.exceptionHandler;

import com.example.reactiveflashcards.api.controller.response.ErrorFieldResponse;
import com.example.reactiveflashcards.api.controller.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@RequiredArgsConstructor
public abstract class AbstractHandleException<T extends Throwable> {
    protected final ObjectMapper objectMapper;

    protected abstract Mono<Void> handle(final ServerWebExchange exchange, final T ex);

    protected abstract boolean accept(final Throwable ex);

    protected Mono<Void> writeResponse(final ServerWebExchange exchange, final ErrorResponse errorResponse) {
        return exchange.getResponse()
                .writeWith(Mono.fromCallable(() -> new DefaultDataBufferFactory()
                        .wrap(objectMapper.writeValueAsBytes(errorResponse))));
    }

    protected void prepareExchange(final ServerWebExchange exchange, final HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().setContentType(APPLICATION_JSON);
    }

    protected ErrorResponse builderError(final HttpStatus status, final String description) {
        return ErrorResponse.builder()
                .httpStatus(status.value())
                .description(description)
                .build();
    }

    protected Mono<ErrorResponse> buildParamErrorMessage(final ErrorResponse response, final ConstraintViolationException ex) {
        return Flux.fromIterable(ex.getConstraintViolations())
                .map(constraintViolation -> ErrorFieldResponse.builder()
                        .name(((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().toString())
                        .message(constraintViolation.getMessage())
                        .build())
                .collectList()
                .map(errorResponses -> response.toBuilder().fields(errorResponses).build());
    }

}