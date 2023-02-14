package com.example.reactiveflashcards.domain.repository;

import com.example.reactiveflashcards.domain.document.DeckDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckRepository extends ReactiveMongoRepository<DeckDocument, String> {
}
