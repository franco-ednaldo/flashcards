package com.example.reactiveflashcards.api.controller.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record ErrorResponse(Integer httpStatus,
                            OffsetDateTime timestamp,
                            String description,
                            List<ErrorFieldResponse> fields) {

    @Builder(toBuilder = true)
    public ErrorResponse {}

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "httpStatus=" + httpStatus +
                ", timestamp=" + timestamp +
                ", description='" + description + '\'' +
                ", fields=" + fields +
                '}';
    }
}
