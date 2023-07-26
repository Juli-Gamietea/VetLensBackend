package com.api.vetlens.entity.questionary;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Answer {
    private String answer;
    private String help;
    private Question embeddedQuestion;
    private Weight weights;
}
