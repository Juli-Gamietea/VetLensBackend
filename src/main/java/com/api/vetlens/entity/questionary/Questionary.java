package com.api.vetlens.entity.questionary;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class Questionary {
    @Id
    private String id;
    private List<Question> questions;
}
