package com.example.reactiveflashcards.domain.document;

import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.OffsetDateTime;

@Document(collation = "users")
public record UserDocument(
        @Id String id,
        String name,
        String email,
        @CreatedDate
        @Field("create_at")
        OffsetDateTime createdAt,
        @LastModifiedDate
        @Field("update_at")
        OffsetDateTime updatedAt
) {
        @Builder(toBuilder = true)
        public UserDocument {}
}
