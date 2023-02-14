package com.example.reactiveflashcards.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, METHOD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {MongoIdValidator.class})
public @interface MongoId {
    String message() default "{com.example.reactive-flashcards.mongoid.message0}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payloads() default {};
}
