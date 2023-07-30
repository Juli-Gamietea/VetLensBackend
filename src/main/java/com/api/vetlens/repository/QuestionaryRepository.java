package com.api.vetlens.repository;

import com.api.vetlens.entity.questionary.Questionary;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionaryRepository extends MongoRepository<Questionary, String> {
}
