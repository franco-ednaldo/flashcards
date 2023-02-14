package com.example.reactiveflashcards.api.mapper;

import com.example.reactiveflashcards.api.controller.request.UserRequest;
import com.example.reactiveflashcards.api.controller.response.UserResponse;
import com.example.reactiveflashcards.domain.document.UserDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserDocument toDocument(UserRequest request);

    UserResponse toResponse(UserDocument document);
}
