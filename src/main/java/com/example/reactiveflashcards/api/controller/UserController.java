package com.example.reactiveflashcards.api.controller;

import com.example.reactiveflashcards.api.controller.request.UserRequest;
import com.example.reactiveflashcards.api.controller.response.UserResponse;
import com.example.reactiveflashcards.api.mapper.UserMapper;
import com.example.reactiveflashcards.domain.service.UserService;
import com.example.reactiveflashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    private final UserQueryService userQueryService;

    private final UserMapper userMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<UserResponse> save(@Valid @RequestBody final UserRequest request) {
        return this.userService.save(this.userMapper.toDocument(request))
                .doFirst(() -> log.info("=== try to save user {}  ===", request))
                .map(this.userMapper::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<UserResponse> findById(@PathVariable final String id) {
        return this.userQueryService.findById(id)
                .map(this.userMapper::toResponse);
    }

}
