package com.api.vetlens.entity.questionary;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "#{questionRepository.getCollectionName()}")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class Question {
    private ObjectId id;
    private String question;
    private String help;
    private List<Answer> answers;
}
