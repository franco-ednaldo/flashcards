package com.example.reactiveflashcards.domain.service.query;

import com.example.reactiveflashcards.domain.document.UserDocument;
import com.example.reactiveflashcards.domain.repository.UserRepository;
import com.example.reactiveflashcards.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.example.reactiveflashcards.exception.BaseErrorMessage.GENERIC_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;

    public Mono<UserDocument> findById(final String id) {
        return this.userRepository.findById(id)
                .doFirst(() -> log.info("=== try to find user with id {}", id))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(GENERIC_NOT_FOUND.params(id).getMessage()))));
    }
}
