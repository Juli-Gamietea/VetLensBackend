package com.api.vetlens.service;

import com.api.vetlens.dto.questionary.QuestionDTO;
import com.api.vetlens.entity.questionary.Answer;
import com.api.vetlens.entity.questionary.Question;
import com.api.vetlens.entity.questionary.Weight;
import com.api.vetlens.exceptions.ApiException;
import com.api.vetlens.repository.QuestionRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final QuestionRepository questionRepository;
    private final ModelMapper mapper = new ModelMapper();
    private final Environment environment;
    public List<QuestionDTO> getQuestions(){
        List<Question> questions = questionRepository.findAll();
        return questions.stream().map(
                question ->  mapper.map(question, QuestionDTO.class)
        ).collect(Collectors.toList());
    }

    public MongoDatabase getMongoDatabase(){
        try{
            return MongoClients.create(Objects.requireNonNull(environment.getProperty("spring.data.mongodb.uri"))).getDatabase("vetlens");
        }catch (Exception e){
            throw new ApiException("No se pudo conectar a MongoDB");
        }
    }
}
