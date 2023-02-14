package com.example.reactiveflashcards.domain.service;

import com.example.reactiveflashcards.domain.document.UserDocument;
import com.example.reactiveflashcards.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<UserDocument> save(final UserDocument user) {
        return this.userRepository.save(user)
                .doFirst(() -> log.info("=== Try to save a follow document {} ===", user));
    }
}
