package com.api.vetlens.repository;

import com.api.vetlens.entity.questionary.Question;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionRepository extends MongoRepository<Question, ObjectId> {
    @Override
    List<Question> findAll();
}
