package com.api.vetlens.dto.questionary;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AnswerDTO {
    private String answer;
    private String help;
    @JsonProperty("embedded_question")
    private QuestionDTO embeddedQuestion;
}
