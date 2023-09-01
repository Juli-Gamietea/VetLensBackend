package com.api.vetlens.repository;

import com.api.vetlens.entity.questionary.Question;
import com.api.vetlens.entity.questionary.Questionary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuestionaryRepository extends MongoRepository<Questionary, String> {
    @Override
    Optional<Questionary> findById(String id);
}
