package com.api.vetlens.service;

import com.api.vetlens.entity.Questionary;
import com.api.vetlens.repository.QuestionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionaryService {
    private final QuestionaryRepository questionaryRepository;

    public Questionary testDB (){
        return questionaryRepository.save(new Questionary("1","Como estas", "Bien"));
    }
}
