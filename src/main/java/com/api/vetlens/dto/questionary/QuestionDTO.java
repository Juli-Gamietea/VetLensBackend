package com.api.vetlens.dto.questionary;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class QuestionDTO {
    private ObjectId id;
    private String question;
    private String help;
    private List<AnswerDTO> answers;
}
